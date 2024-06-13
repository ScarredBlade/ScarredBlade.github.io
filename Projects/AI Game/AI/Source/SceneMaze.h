#ifndef SCENE_MAZE_H
#define SCENE_MAZE_H

#include "GameObject.h"
#include <vector>
#include <queue>
#include "SceneBase.h"
#include "SceneTurn.h"
#include "Maze.h"
#include <unordered_set>

enum class Turn
{
	PLAYER,
	AI
};

class SceneMaze : public SceneBase
{
public:
	SceneMaze();
	~SceneMaze();

	virtual void Init();
	virtual void Update(double dt);
	virtual void Render();
	virtual void Exit();

	void RenderGO(GameObject *go);
	GameObject* FetchGO(GameObject::GAMEOBJECT_TYPE type);

	void DFS(const MazePt& curr);
	bool BFS(const MazePt& start, const MazePt& end);
	void DFSOnce(GameObject* go);

protected:
	int IsWithinBoundary(int x) const;
	int Get1DIndex(int x, int y) const;
	MazePt Get2DIndex(int x) const;
	void PlayerTurn(double dt);
	void AITurn(double dt);
	void MovePlayerAlongPath();

	std::vector<GameObject *> m_goList;
	float m_speed;
	float m_worldWidth;
	float m_worldHeight;
	int m_objectCount;
	int m_noGrid;
	float m_gridSize;
	float m_gridOffset;
	bool m_playerMoved; // Bool to keep track if player has made a move
	bool isLevelPlayable;
	int m_completedLevels = 0;
	bool isLevelComplete;
	int exitPos;
	bool isVisibilityOn;
	std::string m_npcState;
	int tile_cost;

	Maze m_maze;
	MazePt m_start;
	MazePt m_end;
	Turn m_currentTurn; // Keeps track of whose turn it is
	std::vector<Maze::TILE_CONTENT> m_myGrid; //read maze and store here
	std::vector<bool> m_visited; //visited set for DFS/BFS
	std::queue<MazePt> m_queue; //queue for BFS
	std::vector<MazePt> m_previous; //to store previous tile
	std::vector<MazePt> m_shortestPath; //to store shortest path
	std::vector<MazePt> m_findExit;
	unsigned m_mazeKey;
};

#endif