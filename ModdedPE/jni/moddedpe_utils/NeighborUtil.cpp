#include "NeighborUtil.h"

NeighborUtil::NeighborUtil(BlockSource&blockSource)
{
	this->blockSource=&blockSource;
}
bool NeighborUtil::isFullBlockAt(BlockPos const&pos)
{
	int const x=pos.x,y=pos.y,z=pos.z;
	
	return blockSource->getBlock(x,y,z)->isSolid();
}
bool NeighborUtil::isFullBlockAround(BlockPos const&pos)
{
	return (isFullBlockAt({pos.x+1,pos.y,pos.z})||
	isFullBlockAt({pos.x-1,pos.y,pos.z})||
	isFullBlockAt({pos.x,pos.y,pos.z+1})||
	isFullBlockAt({pos.x,pos.y,pos.z-1}));
}
bool NeighborUtil::isShouldRedstoneWireBlockConnectionBlockAt(BlockPos const&pos)
{
	int const x=pos.x,y=pos.y,z=pos.z;
	
	return(blockSource->getBlock(x,y,z)==Block::mRedStoneDust||
	blockSource->getBlock(x,y,z)==Block::mLever||
	blockSource->getBlock(x,y,z)==Block::mStonePressurePlate||
	blockSource->getBlock(x,y,z)==Block::mWoodPressurePlate||
	blockSource->getBlock(x,y,z)==Block::mUnlitRedStoneTorch||
	blockSource->getBlock(x,y,z)==Block::mLitRedStoneTorch||
	blockSource->getBlock(x,y,z)==Block::mStoneButton||
	blockSource->getBlock(x,y,z)==Block::mTripwireHook||
	blockSource->getBlock(x,y,z)==Block::mWoodButton||
	blockSource->getBlock(x,y,z)==Block::mLightWeightedPressurePlate||
	blockSource->getBlock(x,y,z)==Block::mHeavyWeightedPressurePlate||
	blockSource->getBlock(x,y,z)==Block::mDaylightDetector||
	blockSource->getBlock(x,y,z)==Block::mRedstoneBlock||
	blockSource->getBlock(x,y,z)==Block::mUnpoweredComparator||
	blockSource->getBlock(x,y,z)==Block::mPoweredComparator||
	blockSource->getBlock(x,y,z)==Block::mDetectorRail||
	blockSource->getBlock(x,y,z)==Block::mDaylightDetectorInverted);
}
bool NeighborUtil::isShouldRedstoneWireBlockConnectionBlockAround(BlockPos const&pos)
{
	int const x=pos.x,y=pos.y,z=pos.z;
	if(isShouldRedstoneWireBlockConnectionBlockAt({pos.x+1,pos.y,pos.z})||
	isShouldRedstoneWireBlockConnectionBlockAt({pos.x-1,pos.y,pos.z})||
	isShouldRedstoneWireBlockConnectionBlockAt({pos.x,pos.y,pos.z+1})||
	isShouldRedstoneWireBlockConnectionBlockAt({pos.x,pos.y,pos.z-1}))
		return true;
	if(blockSource->getBlock(x+1,y,z)==Block::mUnpoweredRepeater)
	{
		if(blockSource->getData(x+1,y,z)%2==1)
			return true;
	}
	if(blockSource->getBlock(x-1,y,z)==Block::mUnpoweredRepeater)
	{
		if(blockSource->getData(x-1,y,z)%2==1)
			return true;
	}
	if(blockSource->getBlock(x,y,z+1)==Block::mUnpoweredRepeater)
	{
		if(blockSource->getData(x,y,z+1)%2==0)
			return true;
	}
	if(blockSource->getBlock(x,y,z-1)==Block::mUnpoweredRepeater)
	{
		if(blockSource->getData(x,y,z-1)%2==0)
			return true;
	}
	if(blockSource->getBlock(x+1,y,z)==Block::mPoweredRepeater)
	{
		if(blockSource->getData(x+1,y,z)%2==1)
			return true;
	}
	if(blockSource->getBlock(x-1,y,z)==Block::mPoweredRepeater)
	{
		if(blockSource->getData(x-1,y,z)%2==1)
			return true;
	}
	if(blockSource->getBlock(x,y,z+1)==Block::mPoweredRepeater)
	{
		if(blockSource->getData(x,y,z+1)%2==0)
			return true;
	}
	if(blockSource->getBlock(x,y,z-1)==Block::mPoweredRepeater)
	{
		if(blockSource->getData(x,y,z-1)%2==0)
			return true;
	}
	if(blockSource->getBlock(x-1,y,z)==Block::mPiston||blockSource->getBlock(x-1,y,z)==Block::mStickyPiston)
	{
		if(blockSource->getData(x-1,y,z)!=4)
			return true;
	}
	if(blockSource->getBlock(x+1,y,z)==Block::mPiston||blockSource->getBlock(x+1,y,z)==Block::mStickyPiston)
	{
		if(blockSource->getData(x+1,y,z)!=5)
			return true;
	}
	if(blockSource->getBlock(x,y,z-1)==Block::mPiston||blockSource->getBlock(x,y,z-1)==Block::mStickyPiston)
	{
		if(blockSource->getData(x,y,z-1)!=2)
			return true;
	}
	if(blockSource->getBlock(x,y,z+1)==Block::mPiston||blockSource->getBlock(x,y,z+1)==Block::mStickyPiston)
	{
		if(blockSource->getData(x,y,z+1)!=3)
			return true;
	}
	return false;
}
bool NeighborUtil::isRedStoneWireBlockAt(BlockPos const&pos)
{
	int const x=pos.x,y=pos.y,z=pos.z;
	return blockSource->getBlock(x,y,z)==Block::mRedStoneDust;
}
bool NeighborUtil::isRedStoneWireBlockAround(BlockPos const&pos)
{
	int const x=pos.x,y=pos.y,z=pos.z;
	return (isRedStoneWireBlockAt({pos.x+1,pos.y,pos.z})||
	isRedStoneWireBlockAt({pos.x-1,pos.y,pos.z})||
	isRedStoneWireBlockAt({pos.x,pos.y,pos.z+1})||
	isRedStoneWireBlockAt({pos.x,pos.y,pos.z-1}));
}
