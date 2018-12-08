package com.lgren.springboot_mybatisplus_swagger.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * Lgren的部分工具类
 *
 * @author Lgren
 * @create 2018-10-17 1121
 **/
public class LgrenUtil {
    public static <T> LPageInfo<T> pageInfoForList(List<T> list, Integer pageNum, Integer pageSize) {
        List<T> resultList;
        if (CollectionUtils.isEmpty(list)) {
            resultList = new ArrayList<>(0);
            pageNum = 0;
            pageSize = 0;
        } else {
            resultList = list;
        }
        long total = resultList.size();
        int pages = (int) Math.ceil((double) total / Math.max(pageSize, 1));
        if (pageNum > pages) {
            resultList = new ArrayList<>(0);
        } else {
            resultList = list.subList((pageNum - 1) * pageSize, Math.min(pageNum * pageSize, (int) total));
        }
        LPageInfo<T> newPage = new LPageInfo<>();
        newPage.setPageNum(pageNum);// 当前页
        newPage.setPageSize(pageSize);// 每页显示的数量
        newPage.setPages(pages);// 总共页数
        newPage.setTotal(total);// 总数
        newPage.setList(resultList);// 数据

        newPage.setNextPage(pageNum < pageSize ? pageNum + 1 : pageSize);// 下一页
        newPage.setHasNextPage(pageNum < pages);//  是否有下一页
        return newPage;
    }
    @Data
//    @Accessors(chain = true)
    @NoArgsConstructor
    private static class LPageInfo<T> {
        private Integer PageNum;
        private Integer PageSize;
        private Integer Pages;
        private Long Total;
        private Integer NextPage;
        private Boolean HasNextPage = false;
        private List<T> list;
    }
    /**
     * 将 list按照每 pageSize为一段分割
     * @create 2018/11/1 15:04
     * @author Lgren
     * @param list 计划分割的list
     * @param pageSize 每段的长度
     * @param <T>  计划的List<T>的泛型
     * @return 分割好的所有 list
     */

    public static <T> List<List<T>> getAllList(List<T> list, Integer pageSize) {
        int total = list.size();
        int pages = (int) Math.ceil((double) total / Math.max(pageSize, 1));
        List<List<T>> result = new ArrayList<>(pages);
        for (int i = 0; i < pages; i++) {
            result.add(list.subList(i * pageSize, Math.min((i + 1) * pageSize, total)));
        }
        return result;
    }
    /**
     * 遍历 list变成线程分段遍历
     * 相当于将 list.forEach(msg -> {action})
     * 变成     LgrenUtil.execute(executorService, onceNum, action);
     * @create 2018/11/20 17:40
     * @author Lgren
     * @param executorService 例如 :ExecutorService.newFixedThreadPool(4)
     * @param onceNum 一次处理的最大数量 默认最小为 1
     * @param list 需要处理的 list
     * @param action list遍历里边的方法
     * @param <T>  需要处理的 list<T>的泛型
     */
    public static <T> void execute(ExecutorService executorService, Integer onceNum, List<T> list, Consumer<? super T> action) {
        int total = list.size();
        int pages = (int) Math.ceil((double) total / Math.max(onceNum, 1));
        for (int i = 0; i < pages; i++) {
            List<T> ml = list.subList(i * onceNum, Math.min((i + 1) * onceNum, total));
            executorService.execute(() -> ml.forEach(action));
        }
        //        getAllList(list, onceNum).forEach(ml -> executorService.execute(() -> ml.forEach(action)));
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void execute(ExecutorService executorService, Integer time, Runnable command) {
//        for (int i = 0; i < time; i++) {
//            executorService.execute(command);
//        }
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    public static <E> void forEach(Iterable<? extends E> elements, BiConsumer<? super E, Integer> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        int index = 0;
        for (E element : elements) {
            action.accept(element, index++);
        }
    }

    /**
     * 修改 org.springframework.beans.BeanUtils.copyProperties 将里边的 String全部转义
     * @create 2018/10/31 18:27
     * @author Lgren
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        PropertyDescriptor[] var7 = targetPds;
        int var8 = targetPds.length;
        for(int var9 = 0; var9 < var8; ++var9) {
            PropertyDescriptor targetPd = var7[var9];
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            //region 转义修改
                            if (value != null && value.getClass().equals(String.class) && StringUtils.isNotBlank((String) value)) {
                                value = StringEscapeUtils.escapeHtml4((String) value);
                            }
                            //endregion
                            writeMethod.invoke(target, value);
                        } catch (Throwable var15) {
                            throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var15);
                        }
                    }
                }
            }
        }
    }

    public static int calStrLength(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        int CNNum = 0;
        Matcher m = Pattern.compile("[\u4E00-\u9FA5]+").matcher(str);
        while (m.find()) {
            CNNum = m.end();
        }
        return str.length() + 2 * CNNum;
    }

/*    public static <K,V> void forEach(Map<K, V> map, ThiConsumer<? super K, ? super V, Integer> action) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(action);
        int index = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue(), index++);
        }
    }

    public static <K,V> void forEach(Map<K, V> map, BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @FunctionalInterface
    public interface ThiConsumer<T,U,W>{
        void accept(T t, U u, W w);
        default ThiConsumer<T,U,W> andThen(ThiConsumer<? super T,? super U,? super W> consumer){
            return (t, u, w)->{
                accept(t, u, w);
                consumer.accept(t, u, w);
            };
        }
    }*/

//    public static String encode(String input) {
//        if (input == null) {
//            return null;
//        }
//        StringBuilder sb = new StringBuilder(input.length());
//        for (int i = 0, c = input.length(); i < c; i++) {
//            char ch = input.charAt(i);
//            switch (ch) {
//                case '&':
//                    sb.append("&amp;");
//                    break;
//                case '<':
//                    sb.append("&lt;");
//                    break;
//                case '>':
//                    sb.append("&gt;");
//                    break;
//                case '"':
//                    sb.append("&quot;");
//                    break;
//                case '\'':
//                    sb.append("&#x27;");
//                    break;
//                case '/':
//                    sb.append("&#x2F;");
//                    break;
//                default:
//                    sb.append(ch);
//            }
//        }
//        return sb.toString();
//    }


//    public static <T> List<PageInfo<T>> getPageList(List<T> list, Integer pageSize) {
//        List<PageInfo<T>> result = new ArrayList<>();
//        int getPageSize = Math.max(pageSize, 0);
//        PageInfo<T> pageUser = null;
//        do {
//            pageUser = pageInfoForList(list, ofNullable(pageUser).map(PageInfo::getPageNum).orElse(0) + 1, getPageSize);
//            result.add(pageUser);
//        } while (pageUser.isHasNextPage());
//        return result;
//    }


//    public static Map<String, String> getStrObjBetweenOfTwoStr(String str, String start, String end) {
//        Map<String, String> result = new IdentityHashMap<>();
//        if (StringUtils.isBlank(str)) {
//            return result;
//        }
//        start = StringUtils.isBlank(start) ? "{" : start;
//        end = StringUtils.isBlank(end) ? "}" : end;
//        String strVar = str;
//        int startNum = strVar.indexOf(start);
//        int endNum;
//        if (start.equals(end)) {
//            int sstartNumVar = strVar.indexOf(end) + 1;
//            endNum = strVar.substring(sstartNumVar).indexOf(end) + sstartNumVar;
//        } else {
//            endNum = strVar.indexOf(end);
//        }
////        int lastNum = 0;
//        int thisNum = 0;
//        String preStr = "";
//        while(startNum != -1 && endNum != -1) {
//            if (start.equals(end)) {
//                int sstartNumVar = strVar.indexOf(end) + 1;
//                endNum = strVar.substring(sstartNumVar).indexOf(end) + sstartNumVar;
//            } else {
//                endNum = strVar.indexOf(end);
//            }
//            String findStr = strVar.substring(startNum+1, endNum);
////            int[] startAndEnd = {lastNum + startNum + 1, lastNum + endNum};
////            result.put(findStr, startAndEnd);
//            if (thisNum%2 != 0) {
//                result.put(preStr, findStr);
//            }
//            strVar = strVar.substring(endNum + 1);
////            lastNum += endNum + 1;
//            startNum = strVar.indexOf(start);
//            endNum = strVar.indexOf(end);
//            preStr = findStr;
//            thisNum++;
//        }
//        return result;
//    }
//    public static <T> FacetedPage<T> facetedPageLikePageInfoForList(List<T> list, Integer pageNum, Integer pageSize) {
//        if (CollectionUtils.isEmpty(list)) {
//            list = new ArrayList<>(0);
//            pageNum = 0;
//            pageSize = 0;
//        }
//        int total = list.size();
//        int startNum = (pageNum - 1) * pageSize;
//        int endNum = list.size() < pageSize ? list.size() : pageSize;
//        list = list.subList(startNum, startNum + endNum);
//        int pages = (int) Math.ceil(total/(Objects.equals(pageSize, 0) ? 1 : pageSize));
//
//        FacetedPage<T> newPage = new ;
//        newPage.setPageNum(pageNum);// 当前页
//        newPage.setPageSize(pageSize);// 每页显示的数量
//        newPage.setPages(pages);// 总共页数
//        newPage.setTotal(total);// 总数
//        newPage.setList(list);// 数据
//        return newPage;
//    }

//    public static <T> Map<String, Object> mapLikePageInfoForList(List<T> list, Integer pageNum, Integer pageSize) {
//        if (CollectionUtils.isEmpty(list)) {
//            list = new ArrayList<>(0);
//            pageNum = 0;
//            pageSize = 0;
//        }
//        int total = list.size();
//        int startNum = (pageNum - 1) * pageSize;
//        int endNum = list.size() < pageSize ? list.size() : pageSize;
//        list = list.subList(startNum, startNum + endNum);
//        int pages = (int) Math.ceil(total/(Objects.equals(pageSize, 0) ? 1 : pageSize));
//
//        Map<String, Object> resultMap = new HashMap<>(5);
//        resultMap.put("pageNum" ,pageNum);// 当前页
//        resultMap.put("pageSize", pageSize);// 每页显示的数量
//        resultMap.put("pages", pages);// 总共页数
//        resultMap.put("total", total);// 总数
//        resultMap.put("list", list);// 数据
//        return resultMap;
//    }

}
