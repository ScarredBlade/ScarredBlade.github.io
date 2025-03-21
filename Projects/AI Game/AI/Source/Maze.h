#ifndef MAZE_H
#define MAZE_H

#include <vector>

struct MazePt
{
	int x, y;
	MazePt(int _x = 0, int _y = 0) : x(_x), y(_y) {}
	void Set(int _x = 0, int _y = 0) { x = _x; y = _y; }
	bool operator!=(const MazePt rhs) {
		return (x != rhs.x || y != rhs.y);
	}
};

class Maze
{
public:
	enum TILE_CONTENT
	{
		TILE_WALL = -1,
		TILE_FOG = 0,
		TILE_EMPTY = 1,
		TILE_WATER = 2,
		TILE_GRASS = 3,
		TILE_MUD = 4,
		TILE_EXIT = 5
	};
	enum DIRECTION
	{
		DIR_UP,
		DIR_DOWN,
		DIR_LEFT,
		DIR_RIGHT,

		DIR_LEFTUP,
		DIR_LEFTDOWN,
		DIR_RIGHTUP,
		DIR_RIGHTDOWN,
	};
	Maze();
	~Maze();
	void Generate(unsigned key, unsigned size, MazePt start, float wallLoad, float waterLoad, float grassLoad, float mudLoad);
	bool Move(DIRECTION direction);
	unsigned GetKey();
	unsigned GetSize();
	MazePt GetCurr();
	int GetNumMove();
	void SetCurr(MazePt newCurr);
	void SetNumMove(int num);

	TILE_CONTENT See(MazePt tile);
	std::vector<TILE_CONTENT> m_grid;

private:
	// Do not make these public
	unsigned m_key;
	unsigned m_size;
	MazePt m_curr;
	int m_numMove;
};

#endif