#pragma once

#include "mcpe/level/BlockSource.h"
#include "mcpe/util/BlockPos.h"
#include "mcpe/block/Block.h"

class NeighborUtil
{
private:
	BlockSource*blockSource;
public:
	NeighborUtil(BlockSource&);
public:
	bool isFullBlockAt(BlockPos const&);
	bool isFullBlockAround(BlockPos const&);
	bool isRedStoneWireBlockAt(BlockPos const&);
	bool isRedStoneWireBlockAround(BlockPos const&);
	bool isShouldRedstoneWireBlockConnectionBlockAt(BlockPos const&);
	bool isShouldRedstoneWireBlockConnectionBlockAround(BlockPos const&);
};
