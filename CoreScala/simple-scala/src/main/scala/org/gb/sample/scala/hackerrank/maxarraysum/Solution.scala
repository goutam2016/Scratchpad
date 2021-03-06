package org.gb.sample.scala.hackerrank.maxarraysum

import scala.collection.mutable

object Solution {
    def maxSubsetSum(numbers: Array[Int]): Int = {
        val nonAdjMaxSumPerIndex = mutable.Map.empty[Int, Int]
        val nonAdjMaxSumVsIndexArr = numbers.zipWithIndex.reverse
          .map(numVsIdx => (numVsIdx._2, findNonAdjMaxSum(numVsIdx._2, numbers.length - 1, numbers, nonAdjMaxSumPerIndex)))
        return nonAdjMaxSumVsIndexArr.map(_._2).max
    }

    private def findNonAdjMaxSum(currIdx: Int, maxIdx: Int, numbers: Array[Int], nonAdjMaxSumPerIndex: mutable.Map[Int, Int]): Int = {
        val number = numbers(currIdx)
        val nonAdjMaxSum =
            if(currIdx > maxIdx - 2) {
                number
            } else {
                val tmpNonAdjMaxSum = nonAdjMaxSumPerIndex.filter(_._1 > currIdx + 1).values.max
                if(number < 0 || tmpNonAdjMaxSum < 0) {
                    Integer.max(number, tmpNonAdjMaxSum)
                } else {
                    number + tmpNonAdjMaxSum
                }
            }

        nonAdjMaxSumPerIndex.put(currIdx, nonAdjMaxSum)

        if(nonAdjMaxSumPerIndex.size > 3) {
            nonAdjMaxSumPerIndex.remove(currIdx + 3)
        }

        return nonAdjMaxSum
    }
}