#include "Maze.h"
#include <iostream>
#include "MyMath.h"

// DO NOT FOLLOW
// ugly hack: using global var to determine sleep duration (week 7)
int gSleepDuration = 100;

Maze::Maze()
	: m_grid{}, m_key{}, m_size{}, m_curr{}, m_numMove{}
{
}

Maze::~Maze()
{
}

void Maze::Generate(unsigned key, unsigned size, MazePt start, float wallLoad, float waterLoad, float grassLoad, float mudLoad)
{
	m_grid.clear();
	if (size == 0)
		return;
	start.x = Math::Clamp(start.x, 0, (int)size - 1);
	start.y = Math::Clamp(start.y, 0, (int)size - 1);
	wallLoad = Math::Clamp(wallLoad, 0.f, 0.8f);
	waterLoad = Math::Clamp(waterLoad, 0.f, 0.8f);
	grassLoad = Math::Clamp(grassLoad, 0.f, 0.8f);
	mudLoad = Math::Clamp(mudLoad, 0.f, 0.8f);
	unsigned total = size * size;
	m_grid.resize(total);
	std::fill(m_grid.begin(), m_grid.end(), TILE_EMPTY);
	unsigned startId = start.y * size + start.x;
	srand(key);
	for (int i = 0; i < (int)total * wallLoad;)
	{
		unsigned chosen = rand() % total;
		if (chosen == startId)
			continue;
		if (m_grid[chosen] == TILE_EMPTY)
		{
			m_grid[chosen] = TILE_WALL;
			++i;
		}
	}
	for (int i = 0; i < (int)total * waterLoad;)
	{
		unsigned chosen = rand() % total;
		if (chosen == startId)
			continue;
		if (m_grid[chosen] == TILE_EMPTY)
		{
			m_grid[chosen] = TILE_WATER;
			++i;
		}
	}
	for (int i = 0; i < (int)total * grassLoad;)
	{
		unsigned chosen = rand() % total;
		if (chosen == startId)
			continue;
		if (m_grid[chosen] == TILE_EMPTY)
		{
			m_grid[chosen] = TILE_GRASS;
			++i;
		}
	}
	for (int i = 0; i < (int)total * mudLoad;)
	{
		unsigned chosen = rand() % total;
		if (chosen == startId)
			continue;
		if (m_grid[chosen] == TILE_EMPTY)
		{
			m_grid[chosen] = TILE_MUD;
			++i;
		}
	}
	while (true)
	{
		unsigned chosen = rand() % total;
		if (chosen == startId)
			continue;
		if (m_grid[chosen] == TILE_EMPTY)
		{
			m_grid[chosen] = TILE_EXIT;
			break;
		}
	}
	std::cout << "Maze " << key << std::endl;
	for (int row = (int)size - 1; row >= 0; --row)
	{
		for (int col = 0; col < (int)size; ++col)
		{
			switch (m_grid[row * size + col])
			{
			case TILE_WALL:
				std::cout << "W ";
				break;
			case TILE_WATER:
				std::cout << "L ";
				break;
			case TILE_GRASS:
				std::cout << "G ";
				break;
			case TILE_MUD:
				std::cout << "M ";
				break;
			case TILE_EXIT:
				std::cout << "E ";
				break;
			default:
				std::cout << "0 ";
				break;
			}
		}
		std::cout << std::endl;
	}
	m_key = key;
	m_size = size;
	m_numMove = 0;
}

unsigned Maze::GetKey()
{
	return m_key;
}

unsigned Maze::GetSize()
{
	return m_size;
}

MazePt Maze::GetCurr()
{
	return m_curr;
}

int Maze::GetNumMove()
{
	return m_numMove;
}

void Maze::SetCurr(MazePt newCurr)
{
	//++m_numMove;
	m_curr = newCurr;
}

void Maze::SetNumMove(int num)
{
	m_numMove = num;
}

bool Maze::Move(DIRECTION direction)
{
	//++m_numMove;
	MazePt temp = m_curr;
	switch (direction)
	{
	case DIR_LEFT:
		if (temp.x == 0)
			return false;
		temp.x -= 1;
		break;
	case DIR_RIGHT:
		if (temp.x == (int)m_size - 1)
			return false;
		temp.x += 1;
		break;
	case DIR_UP:
		if (temp.y == (int)m_size - 1)
			return false;
		temp.y += 1;
		break;
	case DIR_DOWN:
		if (temp.y == 0)
			return false;
		temp.y -= 1;
		break;
	case DIR_LEFTUP:
		break;
	case DIR_LEFTDOWN:
		break;
	case DIR_RIGHTUP:
		break;
	case DIR_RIGHTDOWN:
		break;
	}
	int tempId = temp.y * m_size + temp.x;
	TILE_CONTENT tileContent = m_grid[tempId];
	if (tileContent == TILE_WALL)
		return false;
	m_curr = temp;
	return true;
}

Maze::TILE_CONTENT Maze::See(MazePt tile)
{
	// Week 8
	// This function will tell the user the content of the specified position

	// Ensure provided position is not out of bound
	if (tile.x < 0 || tile.x >= static_cast<int>(m_size) ||
		tile.y < 0 || tile.y >= static_cast<int>(m_size))
		return TILE_CONTENT::TILE_WALL;

	return m_grid[tile.y * m_size + tile.x];
}