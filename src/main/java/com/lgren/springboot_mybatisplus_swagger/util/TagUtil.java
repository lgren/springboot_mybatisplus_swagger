package com.lgren.springboot_mybatisplus_swagger.util;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.lgren.springboot_mybatisplus_swagger.entity.TagInfo;
import com.lgren.springboot_mybatisplus_swagger.entity.TagLeaf;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * 标签使用的工具类
 * @author Lgren
 * @create 2018-11-21 18:21
 **/
public class TagUtil {
    //    private Map<Integer, TagInfo> tagInfoAllMap;
    private Map<Integer, TagInfo> tagAll;
    //    private Map<Integer, TagLeaf> tagLeafAllMap;

    public TagUtil(Map<Integer, TagInfo> tagAll) {
        this.tagAll = tagAll;
    }

    public List<TagInfo> parseTagStrToList(String tagStr) {
        Map<Integer, List<Integer>> mapVar = TagUtil.parseTagStr(tagStr);
        List<TagInfo> tInfoList = new ArrayList<>(mapVar.size());
        // 遍历 mapVar
        // key:tagInfoId value:对应下所有的tagLeafIdList
        mapVar.forEach((tInfoId, tLeafTagIndexList) ->
                // 如果 mapVar.tInfoId在 tagAll中存在则下一步
                ofNullable(tagAll.get(tInfoId)).ifPresent(tInfoWithAllLeaf ->
                        // 在结果返回值中添加数据tInfoList
                        tInfoList.add(new TagInfo(tInfoWithAllLeaf)
                                // 将mapVar.tLeafTagIndexList换成对应了TagLeafList 并赋值进TagInfo
                                .setTagLeafList(tLeafTagIndexList.stream()
                                        .map(tInfoWithAllLeaf.getTagLeafMap()::get).collect(Collectors.toList())))));
        return tInfoList;
    }

    // 生成特定字符串
    public static String getTagStr(Map<Integer, List<Integer>> map) {
        char[] tagArr = new char[50];
        for (int i = 0, size = tagArr.length; i < size; i++) {
            tagArr[i] = '0';
        }
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            tagArr[entry.getKey() - 1] = toChar(sum(entry.getValue()));
        }
        return new String(tagArr);
    }

    // 解析特定字符串
    public static Map<Integer, List<Integer>> parseTagStr(String tagStr) {
        Map<Integer, List<Integer>> resultMap = new HashMap<>();
        char[] charArr = tagStr.toCharArray();
        for (int i = 0, size = charArr.length; i < size; i++) {
            if (charArr[i] != '0') {
                resultMap.put(i + 1, parseChar(toInt(charArr[i])));
            }
        }
        return resultMap;
    }

    //region 进制生成/解析基础方法
    private int sum(int... intArr) {
        Objects.requireNonNull(intArr);
        int sum = 0;
        for (Integer intVar : intArr) {
            sum += intVar;
        }
        return sum;
    }

    private static int sum(Collection<Integer> intList) {
        Objects.requireNonNull(intList);
        int sum = 0;
        for (Integer intVar : intList) {
            sum += intVar;
        }
        return sum;
    }

    private static char toChar(int numVar) {
        return numVar < 10 ? ((char) (numVar + 48)) : numVar < 36 ? ((char) (numVar + 55)) : numVar < 62 ? ((char) (numVar + 61)) : ((char) (numVar + 19906));
    }

    private Map<Integer, Character> toCharArr(int... numArr) {
        Map<Integer, Character> resultMap = new HashMap<>(numArr.length);
        for (int aNumArr : numArr) {
            resultMap.put(aNumArr, toChar(aNumArr));
        }
        return resultMap;
    }

    private Map<Integer, Character> toCharArr(Collection<Integer> numArr) {
        Map<Integer, Character> resultMap = new HashMap<>(numArr.size());
        for (int aNumArr : numArr) {
            resultMap.put(aNumArr, toChar(aNumArr));
        }
        return resultMap;
    }

    private static int toInt(char charVar) {
        return charVar < 58 ? charVar - 48 : charVar < 91 ? charVar - 55 : charVar < 123 ? charVar - 61 : charVar - 19906;
    }

    private static List<Integer> parseChar(int intVar) {
        int miNum = 0;
        while ((intVar >> miNum) > 1) {
            miNum++;
        }
        List<Integer> resultList = new ArrayList<>(5);
        int intVarVar = intVar;
        do {
            int num = 1 << miNum;
            if (intVarVar >= num) {
                intVarVar -= num;
                resultList.add(num);
                if (intVarVar == num) break;
            }
            miNum--;
        } while (intVarVar > 0);
        return resultList;
    }

    private Map<Character, List<Integer>> toIntArr(Collection<Character> charArr) {
        Map<Character, List<Integer>> resultMap = new HashMap<>(charArr.size());
        for (char charVar : charArr) {
            resultMap.put(charVar, parseChar(toInt(charVar)));
        }
        return resultMap;
    }
    //endregion


    private static int[] normalTag = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 18, 21};
    private static int[] normalTimeTag = {12, 13, 14, 15, 16, 17, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private static int[] likeTag = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48};

    // 随机生成
    public Map<Integer, List<Integer>> randomTag() {
        Map<Integer, List<Integer>> result = new HashMap<>();
        Random random = new Random();
        for (int nTag : normalTag) {
            result.put(nTag, Lists.newArrayList(random.nextBoolean() ? 1 : 0));
        }
        for (int nTag : likeTag) {
            TagInfo tagInfo = tagAll.get(nTag);
            if (tagInfo != null) {
                List<TagLeaf> tLeafAllList = tagInfo.getTagLeafList();
                List<Integer> listVar = new ArrayList<>(tLeafAllList.size());
                tLeafAllList.forEach(t -> {
                    if (random.nextBoolean()) {
                        listVar.add(t.getTagIdIndex());
                    }
                });
                result.put(nTag, listVar);
            }
        }
        return result;
    }
}
