#define NOMINMAX
#include "SceneMaze.h"
#include "GL\glew.h"
#include "Application.h"
#include <stack>
#include <sstream>
#include <iostream>

//ugly hack: do not follow.
extern int gSleepDuration; //declare existence of global var defined in Maze.cpp

SceneMaze::SceneMaze()
	: m_goList{}, m_speed{}, m_worldWidth{},
	  m_worldHeight{}, m_objectCount{}, m_noGrid{}, 
	m_gridSize{}, m_gridOffset{}, m_playerMoved{}, m_maze{},
	m_start{}, m_end{}, m_currentTurn{}, m_myGrid{},
	  m_visited{}, m_queue{}, m_previous{}, 
	  m_shortestPath{}, m_mazeKey{}
{
}

SceneMaze::~SceneMaze()
{
}

void SceneMaze::Init()
{
	SceneBase::Init();
	bLightEnabled = true;

	//Calculating aspect ratio
	m_worldHeight = 100.f;
	m_worldWidth = m_worldHeight * (float)Application::GetWindowWidth() / Application::GetWindowHeight();

	//Physics code here
	m_speed = 1.f;

	Math::InitRNG();

	m_noGrid = 20;
	m_gridSize = m_worldHeight / m_noGrid;
	m_gridOffset = m_gridSize / 2;

	//week 7
	m_start.Set(0, 0);                                     //set agent's starting position
	m_maze.SetCurr(m_start);
	m_mazeKey = time(nullptr);                             //seed for maze generation (use current time as seed)
	srand(m_mazeKey);                                       //seed the random number generator
	m_maze.Generate(m_mazeKey, m_noGrid, m_start, 0.5f, 0.1f, 0.2f, 0.1f); //generate a maze using the provided function
	m_myGrid.resize(m_noGrid * m_noGrid);                 //this is the maze from the agent's POV. an unexplored maze is just FOG to the agent
	m_visited.resize(m_noGrid * m_noGrid);
	m_previous.resize(m_noGrid * m_noGrid);               //this is used by BFS to retrace the found path from endpt to startpoint
	std::fill(m_myGrid.begin(), m_myGrid.end(), Maze::TILE_FOG);
	std::fill(m_visited.begin(), m_visited.end(), false);
	m_myGrid[m_start.y * m_noGrid + m_start.x] = Maze::TILE_EMPTY; //assume first cell is empty(guaranteed by m_maze.Generate)
	m_myGrid = m_maze.m_grid;
	//DFS(m_start);

	// Starts the game with the Player not making a move yet
	m_playerMoved = false;
	// Starts the game with the Player's Turn first
	m_currentTurn = Turn::PLAYER;

	isLevelPlayable = false;

	isVisibilityOn = false;

	isLevelComplete = false;

	//spawn npc
	GameObject* go = FetchGO(GameObject::GAMEOBJECT_TYPE::GO_NPC);
	go->grid.resize(m_noGrid * m_noGrid);
	go->visited.resize(m_noGrid * m_noGrid);
	std::fill(go->grid.begin(), go->grid.end(), Maze::TILE_FOG);
	std::fill(go->visited.begin(), go->visited.end(), false);
	//set position to a random EMPTY tile
	do
	{
		go->curr.Set(Math::RandIntMinMax(0, m_noGrid - 1), Math::RandIntMinMax(0, m_noGrid - 1));
	}
	while (m_maze.See(go->curr) != Maze::TILE_EMPTY);
	go->grid[Get1DIndex(go->curr.x, go->curr.y)] = Maze::TILE_EMPTY;
	go->stack.push_back(go->curr); //triggers dfs

	for (size_t i = 0; i < m_myGrid.size(); ++i)
	{
		if (m_myGrid[i] == Maze::TILE_EXIT)
		{
			exitPos = i;
		}
	}

	m_findExit.clear();
	m_shortestPath.clear();
}

GameObject* SceneMaze::FetchGO(GameObject::GAMEOBJECT_TYPE type)
{
	for (std::vector<GameObject*>::iterator it = m_goList.begin(); it != m_goList.end(); ++it)
	{
		GameObject* go = (GameObject*)*it;
		if (!go->active)
		{
			go->active = true;
			go->type = type;
			++m_objectCount;
			return go;
		}
	}
	for (unsigned i = 0; i < 10; ++i)
	{
		GameObject* go = new GameObject(type);
		m_goList.push_back(go);
	}
	return FetchGO(type);
}

void SceneMaze::Update(double dt)
{
	//std::cout << m_maze.GetCurr().x << "x           y" << m_maze.GetCurr().x;
	SceneBase::Update(dt);

	// Check for tab key press to toggle visibility
	static bool showVisibility = false;
	if (!showVisibility && Application::IsKeyPressed(VK_TAB))
	{
		if (!isVisibilityOn)
		{
			isVisibilityOn = true;
		}
		else if (isVisibilityOn)
		{
			isVisibilityOn = false;
		}
		showVisibility = true;
	}
	else if (showVisibility && !Application::IsKeyPressed(VK_TAB))
	{
		showVisibility = false;
	}

	// Ensure there is a path from start to exit
	while (!isLevelPlayable)
	{
		m_maze.SetNumMove(8);
		for (size_t i = 0; i < m_findExit.size(); ++i)
		{
			if (m_maze.See(m_findExit[i]) == Maze::TILE_EXIT)
			{
				isLevelPlayable = true;
				//std::cout << "playable\n";
				break;
			}
		}
		//std::cout << "not playable\n";
		if (isLevelPlayable) // Check if the condition is met
		{
			for (size_t i = 0; i < m_myGrid.size(); ++i)
			{
				if (m_myGrid[i] == Maze::TILE_EXIT)
				{
					exitPos = i;
					break;
				}
			}
			break; // Exit the while loop
		}

		m_mazeKey = rand();
		m_maze.Generate(m_mazeKey, m_noGrid, m_start, 0.3f, 0.2f, 0.2f, 0.2f);
		m_findExit.clear();
		//m_maze.SetNumMove(8);
		std::fill(m_visited.begin(), m_visited.end(), false);
		DFS(m_start);
	}

	// Calculate aspect ratio
	m_worldHeight = 100.f;
	m_worldWidth = m_worldHeight * (float)Application::GetWindowWidth() / Application::GetWindowHeight();

	// Check whose turn it is and call corresponding turn function
	if (m_currentTurn == Turn::PLAYER)
	{
		//m_maze.SetNumMove(8);
		PlayerTurn(dt);
		// If player has moved, switch to AI's turn
		if (m_playerMoved)
		{
			m_currentTurn = Turn::AI;
			if (m_shortestPath.size() <= 0)
			{
				m_maze.SetNumMove(8);
			}
		}
	}
	else
	{
		AITurn(dt);
	}

	if (Get1DIndex(m_maze.GetCurr().x, m_maze.GetCurr().y) == exitPos)
	{
		isLevelComplete = true;
		m_completedLevels++;
	}

	if (isLevelComplete && m_completedLevels != 3)
	{
		isLevelComplete = false;
		Exit();
		Init();
		//std::cout << "CALLING INIT\n";
	}
}

void SceneMaze::RenderGO(GameObject *go)
{
	switch (go->type)
	{
	case GameObject::GO_NPC: //Render GO_NPC
	{
		modelStack.PushMatrix();
		modelStack.Translate(m_gridOffset + m_gridSize * go->curr.x, m_gridOffset + m_gridSize * go->curr.y, 0.5f);
		modelStack.Scale(m_gridSize, m_gridSize, m_gridSize);
		RenderMesh(meshList[GEO_NPC], false);
		modelStack.PopMatrix();
	}
	break;
	}
}

void SceneMaze::Render()
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Projection matrix : Orthographic Projection
	Mtx44 projection;
	projection.SetToOrtho(0, m_worldWidth, 0, m_worldHeight, -10, 10);
	projectionStack.LoadMatrix(projection);

	// Camera matrix
	viewStack.LoadIdentity();
	viewStack.LookAt(
		camera.position.x, camera.position.y, camera.position.z,
		camera.target.x, camera.target.y, camera.target.z,
		camera.up.x, camera.up.y, camera.up.z
		);
	// Model matrix : an identity matrix (model will be at the origin)
	modelStack.LoadIdentity();

	// Get the current position of the player
	MazePt currPos = m_maze.GetCurr();

	// Calculate the diamond-shaped visibility range around the player
	int visibilityRange;
	// Render only the visible tiles if visibility is on
	if (isVisibilityOn)
	{
		visibilityRange = 100;
	}
	else if (!isVisibilityOn)
	{
		visibilityRange = 2;
	}

	int startX = std::max(0, currPos.x - visibilityRange);
	int endX = std::min(m_noGrid - 1, currPos.x + visibilityRange);
	int startY = std::max(0, currPos.y - visibilityRange);
	int endY = std::min(m_noGrid - 1, currPos.y + visibilityRange);

	// Render only the visible tiles
	for (int y = 0; y < m_noGrid; ++y)
	{
		for (int x = 0; x < m_noGrid; ++x)
		{
			size_t i = y * m_noGrid + x;

			// Convert from 1d index to 2d indices
			size_t tX = i % m_noGrid;
			size_t tY = i / m_noGrid;

			// Calculate the distance from the current tile to the player
			int distX = std::abs(static_cast<int>(tX) - static_cast<int>(currPos.x));
			int distY = std::abs(static_cast<int>(tY) - static_cast<int>(currPos.y));

			// Check if the tile is within the diamond-shaped visibility range
			if (distX + distY <= visibilityRange)
			{
				// Calc position of tile using tX and tY
				modelStack.PushMatrix();
				modelStack.Translate(tX * m_gridSize + m_gridOffset, tY * m_gridSize + m_gridOffset, 0.f);
				modelStack.Scale(m_gridSize, m_gridSize, 1.f);

				//// Draw floor
				//modelStack.PushMatrix();
				//modelStack.Translate(0.f, 0.f, -1.f); // Draw floor beneath other stuff
				//RenderMesh(meshList[GEO_FLOOR], false);
				//modelStack.PopMatrix();

				if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_EXIT) {
					// Render grass tile
					RenderMesh(meshList[GEO_EXIT], false);
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_WATER) {
					// Render water tile
					RenderMesh(meshList[GEO_WATER], false);
					// Assign water_tile
					m_myGrid[i] = Maze::TILE_CONTENT::TILE_WATER;
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_GRASS) {
					// Render grass tile
					RenderMesh(meshList[GEO_GRASS], false);
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_MUD) {
					// Render mud tile
					RenderMesh(meshList[GEO_MUD], false);
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_WALL) {
					RenderMesh(meshList[GEO_WALL], false);
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_FOG) {
					meshList[GEO_WHITEQUAD]->material.kAmbient.Set(0.f, 0.f, 0.f); // Framework-specific code: set quad to black color
					RenderMesh(meshList[GEO_WHITEQUAD], true);
				}
				else if (m_myGrid[i] == Maze::TILE_CONTENT::TILE_EMPTY) {
					// Render mud tile
					RenderMesh(meshList[GEO_GRASS], false);
				}
				else if (m_shortestPath.empty() && m_visited[i] && !m_queue.empty()) {
					meshList[GEO_FLOOR]->material.kAmbient.Set(0.2f, 0.8f, 0.2f); // Framework-specific code: set quad to green color
					RenderMesh(meshList[GEO_FLOOR], true);
				}

				modelStack.PopMatrix();
			}
			else
			{
				// Render black quad for tiles outside visibility range
				modelStack.PushMatrix();
				modelStack.Translate(tX * m_gridSize + m_gridOffset, tY * m_gridSize + m_gridOffset, 0.f);
				modelStack.Scale(m_gridSize, m_gridSize, 1.f);
				RenderMesh(meshList[GEO_BLACKQUAD], true);
				modelStack.PopMatrix();
			}
		}
	}

	//render shortest path
	if (!m_shortestPath.empty())
	{
		for (size_t i = 0; i < m_shortestPath.size() - 1; ++i)
		{
			const MazePt& pt = m_shortestPath[i];

			modelStack.PushMatrix();
			modelStack.Translate(pt.x * m_gridSize + m_gridOffset, pt.y * m_gridSize + m_gridOffset, 0.09f);
			modelStack.Scale(m_gridSize, m_gridSize, 1.f);
			RenderMesh(meshList[GEO_WAYPOINT], false);
			modelStack.PopMatrix();

			//todo: cout the shortest path yourself for debugging purpose!
		}
	}

	//render agent's current pos
	modelStack.PushMatrix();
	modelStack.Translate(currPos.x * m_gridSize + m_gridOffset, currPos.y * m_gridSize + m_gridOffset, 0.1f);
	modelStack.Scale(m_gridSize, m_gridSize, 1.f);
	RenderMesh(meshList[GEO_AGENT], false);
	modelStack.PopMatrix();

	//// Calculate the position of the exit at the top right corner of the map
	//float exitPosX = (m_noGrid - 1) * m_gridSize + m_gridOffset;
	//float exitPosY = (m_noGrid - 1) * m_gridSize + m_gridOffset;

	//// Render the exit geometry
	//modelStack.PushMatrix();
	//modelStack.Translate(exitPosX, exitPosY, 0.1f);
	//modelStack.Scale(m_gridSize, m_gridSize, 1.f);
	//RenderMesh(meshList[GEO_EXIT], false); // Assuming GEO_EXIT is the geometry for the exit
	//modelStack.PopMatrix();

	//Render m_goList
	for (GameObject* go : m_goList)
	{
		if (go->active)
			RenderGO(go);
	}

	// On-screen text to display whose turn it is
	std::ostringstream ss;
	ss << "Turn: ";
	if (m_currentTurn == Turn::PLAYER)
	{
		ss << "Player";
	}
	else
	{
		ss << "AI";
	}
	RenderTextOnScreen(meshList[GEO_TEXT], ss.str(), Color(0, 1, 0), 3, 50, 51);

	//ss.str("");
	//ss << "Press SPACE to speed up";
	//RenderTextOnScreen(meshList[GEO_TEXT], ss.str(), Color(0, 1, 0), 3, 50, 6);

	ss.str("");
	ss << "Num Move: " << m_maze.GetNumMove();
	RenderTextOnScreen(meshList[GEO_TEXT], ss.str(), Color(0, 1, 0), 3, 50, 48);

	ss.str("");
	ss << "Maze Seed: " << m_maze.GetKey();
	RenderTextOnScreen(meshList[GEO_TEXT], ss.str(), Color(0, 1, 0), 3, 50, 54);

	if (m_completedLevels == 3)
	{
		ss.str("");
		ss << "3 Levels Completed!";
		RenderTextOnScreen(meshList[GEO_TEXT], ss.str(), Color(1, 0, 0), 3, 50, 27);
	}
}

void SceneMaze::Exit()
{
	SceneBase::Exit();
	//Cleanup GameObjects
	while (m_goList.size() > 0)
	{
		GameObject *go = m_goList.back();
		delete go;
		m_goList.pop_back();
	}
}

//helper function to convert from 2d indices to 1d index
int SceneMaze::Get1DIndex(int x, int y) const
{
	return y * m_noGrid + x;
}

MazePt SceneMaze::Get2DIndex(int x) const
{
	// Convert from 1d index to 2d indices
	size_t tX = x % m_noGrid;
	size_t tY = x / m_noGrid;
	return MazePt(tX, tY);
}

//helper function to check if given index is within boundary
int SceneMaze::IsWithinBoundary(int x) const
{
	return x >= 0 && x < m_noGrid;
}

void SceneMaze::PlayerTurn(double dt)
{
	if (Application::IsKeyPressed(VK_OEM_MINUS))
	{
		m_speed = Math::Max(0.f, m_speed - 0.1f);
	}
	if (Application::IsKeyPressed(VK_OEM_PLUS))
	{
		m_speed += 0.1f;
	}
	if (Application::IsKeyPressed('R'))
	{
		// Exercise: Implement Reset button
	}

	if (!m_shortestPath.empty())
	{
		// Move the player along the shortest path if available
		MovePlayerAlongPath();

		m_playerMoved = true;
		return;
	}

	// Input Section
	if (!m_playerMoved && Application::IsMousePressed(0))
	{
		std::cout << "LBUTTON DOWN" << std::endl;
		double x, y;
		Application::GetCursorPos(&x, &y);
		int w = Application::GetWindowWidth();
		int h = Application::GetWindowHeight();
		float posX = static_cast<float>(x) / w * m_worldWidth;
		float posY = (h - static_cast<float>(y)) / h * m_worldHeight;

		// Check if the clicked position is within the visibility range
		MazePt currPos = m_maze.GetCurr();

		// Calculate the diamond-shaped visibility range around the player
		int visibilityRange;
		// Render only the visible tiles if visibility is on
		if (isVisibilityOn)
		{
			visibilityRange = 100;
		}
		else if (!isVisibilityOn)
		{
			visibilityRange = 2;
		}

		int gridX = static_cast<int>(posX / m_gridSize);
		int gridY = static_cast<int>(posY / m_gridSize);
		int distX = std::abs(gridX - currPos.x);
		int distY = std::abs(gridY - currPos.y);
		if (distX + distY <= visibilityRange)
		{
			//// Check if the player clicked on a wall or water
			Maze::TILE_CONTENT tileContent = m_maze.See({ gridX, gridY });
			//switch (tileContent)
			//{
			//case Maze::TILE_WATER:
			//	// deduct 3 moves for water
			//	//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 3));
			//	tile_cost = 3;
			//	break;
			//case Maze::TILE_GRASS:
			//	// deduct 2 moves for grass
			//	//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 2));
			//	tile_cost = 2;
			//	break;
			//case Maze::TILE_MUD:
			//	// deduct 1 move for mud
			//	//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 1));
			//	tile_cost = 1;
			//	break;
			//default:
			//	break;
			//}
			if (tileContent != Maze::TILE_WALL)
			{
				// Check if the clicked tile is within the visibility range
				BFS(m_maze.GetCurr(), { gridX, gridY });
				//m_playerMoved = true;
			}

			if (m_shortestPath.size() > 0)
			{
				if (m_shortestPath.front().x == m_maze.GetCurr().x && m_shortestPath.front().y == m_maze.GetCurr().y)
				{
					m_shortestPath.erase(m_shortestPath.begin());
				}
			}

			int tempNumMove = m_maze.GetNumMove();

			std::vector<MazePt>::iterator it = m_shortestPath.begin();

			while (it != m_shortestPath.end())
			{
				//// Check if the player clicked on a wall or water
				Maze::TILE_CONTENT tileContent = m_maze.See({ it->x, it->y });
				switch (tileContent)
				{
				case Maze::TILE_WATER:
					// deduct 3 moves for water
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 3));
					tile_cost = 3;
					break;
				case Maze::TILE_GRASS:
					// deduct 2 moves for grass
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 2));
					tile_cost = 2;
					break;
				case Maze::TILE_MUD:
					// deduct 1 move for mud
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 1));
					tile_cost = 1;
					break;
				default:
					break;
				}

				if ((tempNumMove - tile_cost) < 0)
				{
					it = m_shortestPath.erase(it, m_shortestPath.end());
					//m_maze.SetNumMove(8);
					break;
				}
				it++;
				tempNumMove -= tile_cost;
				//m_maze.SetNumMove(m_maze.GetNumMove() - tile_cost);
			}
		}
	}
	else if (m_playerMoved && !Application::IsMousePressed(0))
	{
		m_playerMoved = false;
		std::cout << "LBUTTON UP" << std::endl;
	}

	static bool bRButtonState = false;
	if (!bRButtonState && Application::IsMousePressed(1))
	{
		bRButtonState = true;
		std::cout << "RBUTTON DOWN" << std::endl;
	}
	else if (bRButtonState && !Application::IsMousePressed(1))
	{
		bRButtonState = false;
		std::cout << "RBUTTON UP" << std::endl;
	}

	static bool bSpaceState = false;
	if (!bSpaceState && Application::IsKeyPressed(VK_SPACE))
	{
		bSpaceState = true;
		if (gSleepDuration == 100)
			gSleepDuration = 0;
		else
			gSleepDuration = 100;
	}
	else if (bSpaceState && !Application::IsKeyPressed(VK_SPACE))
	{
		bSpaceState = false;
	}
}

void SceneMaze::AITurn(double dt)
{
	// update movement at fixed time interval
	static constexpr float TURN_TIME = 1.5f;
	static float timer = 0.f;
	timer += static_cast<float>(dt) * m_speed; // keep track of time till next interval
	if (timer >= TURN_TIME)
	{
		// the multiplication at the end is to account for massive lag,
		// should the accumulated time surpass multiples of TURN_TIME
		timer -= TURN_TIME * static_cast<int>(timer / TURN_TIME);

		bool aiMoved = false; // Flag to track if any AI has moved
		for (GameObject* go : m_goList)
		{
			if (go->active && go->type == GameObject::GAMEOBJECT_TYPE::GO_NPC)
			{
				// if stack isn't empty, agent will explore the map
				if (!go->stack.empty())
					DFSOnce(go);
				// otherwise, checks if agent has a path to follow
				else if (!go->path.empty())
				{
					// Move the AI one block along the path
					MazePt node = go->path.front();
					go->path.erase(go->path.begin());
					go->curr = node;
					aiMoved = true;
				}
			}
		}

		// Check if any AI has moved, if not, return control to player's turn
		if (!aiMoved)
		{
			m_currentTurn = Turn::PLAYER;
		}
	}
}

//week 7
//time for the agent to explore the maze
//we're mimicking how in Micromouse, a robot would first explore the given maze
//to build a image of the environment, before being tasked to solve the maze in the shortest time possible
//when the agent finishes exploring, it should end up in the starting spot
void SceneMaze::DFS(const MazePt& curr)
{
	m_visited[Get1DIndex(curr.x, curr.y)] = true; //mark current cell as visited
	m_findExit.push_back(curr);

	//MOVE UP
	//attempt to move up. check if such move is within boundary.
	//in addition, movement is only possible if the next cell is unvisited
	//unless it's on a return trip, this agent will attempt to make each step into uncharted ground
	int nextIndex = Get1DIndex(curr.x, curr.y + 1);
	if (IsWithinBoundary(curr.y + 1) && !m_visited[nextIndex])
	{
		if (m_maze.Move(Maze::DIRECTION::DIR_UP)) //move agent up
		{
			m_myGrid[nextIndex] = m_maze.m_grid[nextIndex];
			DFS(MazePt{ curr.x, curr.y + 1 });
			m_maze.Move(Maze::DIRECTION::DIR_DOWN); //return trip
		}
		else //wall up ahead, can't move to cell
		{
			m_myGrid[nextIndex] = Maze::TILE_CONTENT::TILE_WALL;
		}
	}

	//MOVE DOWN
	nextIndex = Get1DIndex(curr.x, curr.y - 1);
	if (IsWithinBoundary(curr.y - 1) && !m_visited[nextIndex])
	{
		if (m_maze.Move(Maze::DIRECTION::DIR_DOWN))
		{
			m_myGrid[nextIndex] = m_maze.m_grid[nextIndex];
			DFS(MazePt{ curr.x, curr.y - 1 });
			m_maze.Move(Maze::DIRECTION::DIR_UP); //return trip
		}
		else //wall up ahead, can't move to cell
		{
			m_myGrid[nextIndex] = Maze::TILE_CONTENT::TILE_WALL;
		}
	}

	//MOVE LEFT
	nextIndex = Get1DIndex(curr.x - 1, curr.y);
	if (IsWithinBoundary(curr.x - 1) && !m_visited[nextIndex])
	{
		if (m_maze.Move(Maze::DIRECTION::DIR_LEFT))
		{
			m_myGrid[nextIndex] = m_maze.m_grid[nextIndex];
			DFS(MazePt{ curr.x - 1, curr.y });
			m_maze.Move(Maze::DIRECTION::DIR_RIGHT); //return trip
		}
		else //wall up ahead, can't move to cell
		{
			m_myGrid[nextIndex] = Maze::TILE_CONTENT::TILE_WALL;
		}
	}

	//MOVE RIGHT
	nextIndex = Get1DIndex(curr.x + 1, curr.y);
	if (IsWithinBoundary(curr.x + 1) && !m_visited[nextIndex])
	{
		if (m_maze.Move(Maze::DIRECTION::DIR_RIGHT))
		{
			m_myGrid[nextIndex] = m_maze.m_grid[nextIndex];
			DFS(MazePt{ curr.x + 1, curr.y });
			m_maze.Move(Maze::DIRECTION::DIR_LEFT); //return trip
		}
		else //wall up ahead, can't move to cell
		{
			m_myGrid[nextIndex] = Maze::TILE_CONTENT::TILE_WALL;
		}
	}
}

//week 7
//we use BFS to perform pathfinding. It's very brute-force and 
//is not exactly performant. we will explore the A* algorithm
//in future lessons.
bool SceneMaze::BFS(const MazePt& start, const MazePt& end)
{
	// prepare variables
	std::fill(m_visited.begin(), m_visited.end(), false);
	std::fill(m_previous.begin(), m_previous.end(), MazePt{ -1, -1 });
	m_shortestPath.clear();
	m_queue = {};

	m_queue.push(start);
	m_visited[Get1DIndex(start.x, start.y)] = true;

	while (!m_queue.empty())
	{
		MazePt pt{ m_queue.front() };
		m_queue.pop();

		//m_maze.SetCurr(pt);

		if (pt.x == end.x && pt.y == end.y)
		{
			MazePt curr{ pt };
			m_shortestPath.push_back(curr);
			while (curr.x != start.x || curr.y != start.y)
			{
				curr = m_previous[Get1DIndex(curr.x, curr.y)];
				m_shortestPath.insert(m_shortestPath.begin(), curr);
			}
			//m_shortestPath.push_back(end);
			return true;
		}

		// Check UP
		int nextIndex = Get1DIndex(pt.x, pt.y + 1);
		if (IsWithinBoundary(pt.y + 1) && !m_visited[nextIndex] &&
			(m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EMPTY ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_WATER ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_GRASS ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_MUD ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EXIT))
		{
			m_visited[nextIndex] = true;
			m_queue.push(MazePt{ pt.x, pt.y + 1 });
			m_previous[nextIndex] = pt;
		}
		// Check DOWN
		nextIndex = Get1DIndex(pt.x, pt.y - 1);
		if (IsWithinBoundary(pt.y - 1) && !m_visited[nextIndex] &&
			(m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EMPTY ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_WATER ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_GRASS ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_MUD ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EXIT))
		{
			m_visited[nextIndex] = true;
			m_queue.push(MazePt{ pt.x, pt.y - 1 });
			m_previous[nextIndex] = pt;
		}
		// Check LEFT
		nextIndex = Get1DIndex(pt.x - 1, pt.y);
		if (IsWithinBoundary(pt.x - 1) && !m_visited[nextIndex] &&
			(m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EMPTY ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_WATER ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_GRASS ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_MUD ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EXIT))
		{
			m_visited[nextIndex] = true;
			m_queue.push(MazePt{ pt.x - 1, pt.y });
			m_previous[nextIndex] = pt;
		}
		// Check RIGHT
		nextIndex = Get1DIndex(pt.x + 1, pt.y);
		if (IsWithinBoundary(pt.x + 1) && !m_visited[nextIndex] &&
			(m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EMPTY ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_WATER ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_GRASS ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_MUD ||
				m_myGrid[nextIndex] == Maze::TILE_CONTENT::TILE_EXIT))
		{
			m_visited[nextIndex] = true;
			m_queue.push(MazePt{ pt.x + 1, pt.y });
			m_previous[nextIndex] = pt;
		}
	}

	m_maze.SetCurr(start);
	return false;
}

void SceneMaze::DFSOnce(GameObject* go)
{
	MazePt curr{ go->curr.x, go->curr.y };
	std::vector<MazePt>& stack = go->stack; //get a short-hand to stack instead of constant use of indirection later
	if (stack.empty() ||
		stack.back().x != curr.x || stack.back().y != curr.y) //make sure we don't push node into stack when backtracking
		go->stack.push_back(curr);
	go->visited[Get1DIndex(curr.x, curr.y)] = true;           //mark current node as visited

	//offsets: UP, DOWN, LEFT, RIGHT
	static int offsets[][2] = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };

	//check each direction
	//int (& offset)[2] ---> offset is a reference to int[2]
	MazePt next{};
	for (int(&offset)[2] : offsets)
	{
		next.Set(curr.x + offset[0], curr.y + offset[1]);
		//only consider next node if it's unvisited
		if (IsWithinBoundary(next.x) && IsWithinBoundary(next.y) && !go->visited[Get1DIndex(next.x, next.y)])
		{
			//update agent's mental record of the maze
			int idx = Get1DIndex(next.x, next.y);
			Maze::TILE_CONTENT tileContent = m_maze.See(next);
			if (tileContent == Maze::TILE_CONTENT::TILE_EMPTY ||
				tileContent == Maze::TILE_CONTENT::TILE_WATER ||
				tileContent == Maze::TILE_CONTENT::TILE_GRASS ||
				tileContent == Maze::TILE_CONTENT::TILE_MUD)
			{
				go->grid[idx] = tileContent;
				go->curr = next; //let's move to the next cell
				return;
			}
		}
	}

	//already fully explored all surrounding neighbours. time to backtrack
	stack.pop_back();
	if (!stack.empty())
		go->curr = stack.back();
}

void SceneMaze::MovePlayerAlongPath()
{
	if (!m_shortestPath.empty())
	{
		if (m_shortestPath.front().x == m_maze.GetCurr().x && m_shortestPath.front().y == m_maze.GetCurr().y)
		{			
			m_shortestPath.erase(m_shortestPath.begin());
		}

		if (m_shortestPath.size() > 0)
		{
			// Move the player along the path
			MazePt nextPos = m_shortestPath.front();

			// Check if the next position is reachable (not a wall)
			if (m_maze.See(nextPos) != Maze::TILE_WALL)
			{
				Maze::TILE_CONTENT tileContent = m_maze.See(nextPos);
				switch (tileContent)
				{
				case Maze::TILE_WATER:
					// deduct 3 moves for water
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 3));
					tile_cost = 3;
					break;
				case Maze::TILE_GRASS:
					// deduct 2 moves for grass
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 2));
					tile_cost = 2;
					break;
				case Maze::TILE_MUD:
					// deduct 1 move for mud
					//m_maze.SetNumMove(Math::Max(0, m_maze.GetNumMove() - 1));
					tile_cost = 1;
					break;
				default:
					break;
				}
				
				// Move the player to the next position
				m_maze.SetCurr(nextPos);
				m_maze.SetNumMove(m_maze.GetNumMove() - tile_cost);
			}
		}
	}
}