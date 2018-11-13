package cn.kirilenkoo.www.coolpets.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import timber.log.Timber;
@Deprecated
public class FindMedian {
    private static final int THRESHOLD = 10000; // assume this is an OK threshold for ram to copy all the numbers
    private int INITIAL_RANGE = 100000; // which will result in about 21xxx buckets

    public double findMedian(int[] array) {
        Timber.d(array.length+"");
        int[] bucket = bucketSort(array, Integer.MIN_VALUE, Integer.MAX_VALUE, INITIAL_RANGE, -1, 1);
        List<Integer> copy = new ArrayList<>();
        for(int n: array)
            if (n>=bucket[0] && n<=bucket[1]) copy.add(n);
        Collections.sort(copy);
        return 0.5*(copy.get(bucket[2])+copy.get(bucket[3]));
    }

    /*
     * left : index from the current bucket of the first median candidate
     * right : index from the current bucket of the second median candidate
     * range : range of each bucket
     * lo, hi: edge of current range of all buckets
     */
    public int[] bucketSort(int[] array, int lo, int hi, int range, long left, long right) {
        // use a treemap as buckets, value is the count of numbers in each bucket
        // first initiate buckets with certain range
        TreeMap<Integer, Integer> buckets = new TreeMap<>();
        for(int i = lo; i<=hi-range; i+=range)
            buckets.put(i, 0);
        long count = 0;
        // fill buckets
        for(int n: array) {
            count++;
            int key = buckets.floorKey(n);
            buckets.put(key, buckets.get(key) + 1);
        }

        // initialize when it is first sort
        if (right - left > 1) {
            left = (count-1)/2;
            right = count/2;
        }

        /*
         * cntSoFar - sub count from first bucket
         * subCnt - count of numbers in returned bucket(s)
         * leftCnt - count of numbers smaller than numbers in returned bucket(s)
         */
        long cntSoFar = 0, subCnt = 0, leftCnt = 0;
        int leftEdge = hi, rightEdge = lo;
        for(int i = lo; i<=hi-range; i+=range) {
            cntSoFar += buckets.get(i);
            if (cntSoFar >= left && leftEdge == hi) {
                leftEdge = i;
                leftCnt = cntSoFar - buckets.get(i);
            }
            if (leftEdge < hi) subCnt += buckets.get(i);
            if (cntSoFar >= right) {
                rightEdge = (i<=hi-range)?i+range-1:hi;
                break;
            }
        }

        // if count of numbers in returned range is still greater than THRESHOLD, recurse
        // otherwise, return the range and nth to find
        if (subCnt > THRESHOLD) return bucketSort(array, leftEdge, rightEdge, range/10, left-leftCnt, right-leftCnt);
        return new int[]{leftEdge, rightEdge, (int)(left-leftCnt), (int)(right-leftCnt)};
    }

    public void findMed(){
        int[] datas = new int[]{1,2,3,4,4,4,4,4,4,9};
        Timber.d(biSort(datas,0,-100,100).toString());
    }
    public STATE biSort(int[] array, int mid, int left, int right){
        int leftCount = 0;
        int rightCount = 0;
        for(int n: array){
            if(n<mid+0.5)leftCount ++;
            else rightCount ++;
        }
        int s = leftCount - rightCount;
        switch (s){
            case 0: return new STATE(mid, 2);
            case 1: return new STATE(mid, 0);
            case -1: return new STATE(mid, 1);
        }
        if(leftCount>rightCount) return biSort(array,(left+mid)/2,left,mid);
        else return biSort(array, (right+mid)/2, mid, right);
    }
    class STATE{
        int mid = 0;
        int dir = -1; //0 left, 1 right, 2 left and right

        public STATE(int mid, int dir) {
            this.mid = mid;
            this.dir = dir;
        }
        public String toString(){
            return mid+"::"+dir;
        }
    }
}
