package com.quipu.advent_of_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day6 {

    public static void main(String[] args) {
        int[] cyclesToFirstInfiniteLoop = findCyclesToFirstInfiniteLoop(new int[]{0, 2, 7, 0});
        System.out.println("test result = " + Arrays.toString(cyclesToFirstInfiniteLoop));
        cyclesToFirstInfiniteLoop = findCyclesToFirstInfiniteLoop(new int[]{11, 11, 13, 7, 0, 15, 5, 5, 4, 4, 1, 1,
                7, 1, 15, 11});
        System.out.println("part 1 result = " + Arrays.toString(cyclesToFirstInfiniteLoop));
    }

    private static int[] findCyclesToFirstInfiniteLoop(int[] inputBanksState) {
        Memory memory = new Memory(inputBanksState);
        int steps = 0;
        boolean active = true;
        int numberOfLoops = 0;
        while (active) {
            memory.reallocate();
            if (memory.hasLooped()) {
                active = false;
                numberOfLoops = steps - memory.getFirstActualStateIndexFromHistory();
            }
            ++steps;
        }
        return new int[]{steps, numberOfLoops};
    }

    private static class Memory {
        private List<Bank> banks;
        private List<String> history = new ArrayList<>();
        private boolean hasLooped;

        Memory(int[] inputBanksState) {
            banks = Arrays.stream(inputBanksState)
                    .boxed()
                    .map(Bank::new)
                    .collect(Collectors.toList());
            saveStateHistory();
        }

        private void saveStateHistory() {
            String state = getState();
            if (history.contains(state)) {
                hasLooped = true;

            }
            history.add(state);
        }

        private String getState() {
            StringBuilder sb = new StringBuilder();
            for (Bank bank : banks) {
                sb.append(bank.numberOfBlocks);
            }
            return sb.toString();
        }

        void reallocate() {
            Bank maximumAllocatedBank = findMaximumAllocatedBank();
            int numberOfBlocksToAllocate = maximumAllocatedBank.clearBank();
            List<Integer> blocksToAllocate = createBlocksToAllocate(numberOfBlocksToAllocate);
            allocateBlocksToBanks(blocksToAllocate, banks.indexOf(maximumAllocatedBank) + 1);
            saveStateHistory();
        }

        private List<Integer> createBlocksToAllocate(int numberOfBlocksToAllocate) {
            List<Integer> b = new ArrayList<>();
            int blockPerBank = numberOfBlocksToAllocate / banks.size();
            if (blockPerBank == 0) {
                blockPerBank = 1;
            }
            while (numberOfBlocksToAllocate > 0) {
                if (blockPerBank > numberOfBlocksToAllocate) {
                    b.add(numberOfBlocksToAllocate);
                    numberOfBlocksToAllocate = 0;
                } else {
                    b.add(blockPerBank);
                    numberOfBlocksToAllocate -= blockPerBank;
                }
            }
            return b;
        }

        private void allocateBlocksToBanks(List<Integer> blocksToAllocate, int index) {
            if (index == banks.size()) {
                index = 0;
            }
            for (Integer aBlocksToAllocate : blocksToAllocate) {
                banks.get(index++).addBlocks(aBlocksToAllocate);
                if (index == banks.size()) {
                    index = 0;
                }
            }
        }

        private Bank findMaximumAllocatedBank() {
            Bank bank = banks.get(0);
            for (int i = 1; i < banks.size(); i++) {
                if (banks.get(i).numberOfBlocks > bank.numberOfBlocks) {
                    bank = banks.get(i);
                }
            }
            return bank;
        }

        boolean hasLooped() {
            return this.hasLooped;
        }

        int getFirstActualStateIndexFromHistory() {
            return history.indexOf(getState());
        }
    }

    private static class Bank {
        private int numberOfBlocks;

        private Bank(int numberOfBlocks) {
            this.numberOfBlocks = numberOfBlocks;
        }

        int clearBank() {
            int temp = numberOfBlocks;
            this.numberOfBlocks = 0;
            return temp;
        }

        void addBlocks(int blocks) {
            numberOfBlocks += blocks;
        }
    }
}
