package com.xile.script.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SortListUtil {
    public static final String DESC = "desc";
    public static final String ASC = "asc";

    /**
     * 对list中的元素按升序排列.
     *
     * @param list  排序集合
     * @param field 排序字段
     * @return
     */
    public static List<?> sort(List<?> list, final String field) {
        return sort(list, field, null);
    }

    /**
     * 对list中的元素进行排序.
     *
     * @param list  排序集合
     * @param field 排序字段
     * @param sort  排序方式: SortListUtil.DESC(降序) SortListUtil.ASC(升序).
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<?> sort(List<?> list, final String field,
                               final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Field f = a.getClass().getDeclaredField(field);
                    f.setAccessible(true);
                    Class<?> type = f.getType();

                    if (type == int.class) {
                        ret = ((Integer) f.getInt(a)).compareTo(f
                                .getInt(b));
                    } else if (type == double.class) {
                        ret = ((Double) f.getDouble(a)).compareTo(f
                                .getDouble(b));
                    } else if (type == long.class) {
                        ret = ((Long) f.getLong(a)).compareTo(f
                                .getLong(b));
                    } else if (type == float.class) {
                        ret = ((Float) f.getFloat(a)).compareTo(f
                                .getFloat(b));
                    } else if (type == Date.class) {
                        ret = ((Date) f.get(a)).compareTo((Date) f.get(b));
                    } else if (isImplementsOf(type, Comparable.class)) {
                        ret = ((Comparable) f.get(a)).compareTo(f
                                .get(b));
                    } else {
                        ret = String.valueOf(f.get(a)).compareTo(
                                String.valueOf(f.get(b)));
                    }

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (sort != null && sort.toLowerCase().equals(DESC)) {
                    return -ret;
                } else {
                    return ret;
                }

            }
        });
        return list;
    }

    /**
     * 对list中的元素按fields和sorts进行排序,
     * fields[i]指定排序字段,sorts[i]指定排序方式.如果sorts[i]为空则默认按升序排列.
     *
     * @param list   排序集合
     * @param fields 排序字段-数组（一个或多个）
     * @param sorts  排序规则-数组（一个或多个）
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<?> sort(List<?> list, String[] fields, String[] sorts) {
        if (fields != null && fields.length > 0) {
            for (int i = fields.length - 1; i >= 0; i--) {
                final String field = fields[i];
                String tmpSort = ASC;
                if (sorts != null && sorts.length > i && sorts[i] != null) {
                    tmpSort = sorts[i];
                }
                final String sort = tmpSort;
                Collections.sort(list, new Comparator() {
                    public int compare(Object a, Object b) {
                        int ret = 0;
                        try {
                            Field f = a.getClass().getDeclaredField(field);
                            f.setAccessible(true);
                            Class<?> type = f.getType();
                            if (type == int.class) {
                                ret = ((Integer) f.getInt(a))
                                        .compareTo(f.getInt(b));
                            } else if (type == double.class) {
                                ret = ((Double) f.getDouble(a))
                                        .compareTo(f.getDouble(b));
                            } else if (type == long.class) {
                                ret = ((Long) f.getLong(a)).compareTo(f
                                        .getLong(b));
                            } else if (type == float.class) {
                                ret = ((Float) f.getFloat(a))
                                        .compareTo(f.getFloat(b));
                            } else if (type == Date.class) {
                                ret = ((Date) f.get(a)).compareTo((Date) f
                                        .get(b));
                            } else if (isImplementsOf(type, Comparable.class)) {
                                ret = ((Comparable) f.get(a))
                                        .compareTo(f.get(b));
                            } else {
                                ret = String.valueOf(f.get(a)).compareTo(
                                        String.valueOf(f.get(b)));
                            }

                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        if (sort != null && sort.toLowerCase().equals(DESC)) {
                            return -ret;
                        } else {
                            return ret;
                        }
                    }
                });
            }
        }
        return list;
    }

    /**
     * 默认按正序排列
     *
     * @param list   排序集合
     * @param method 排序方法 "getXxx()"
     * @return
     */
    public static List<?> sortByMethod(List<?> list, final String method) {
        return sortByMethod(list, method, null);
    }

    /**
     * 集合排序
     *
     * @param list   排序集合
     * @param method 排序方法 "getXxx()"
     * @param sort   排序方式: SortListUtil.DESC(降序) SortListUtil.ASC(升序).
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<?> sortByMethod(List<?> list, final String method,
                                       final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m = a.getClass().getMethod(method);
                    m.setAccessible(true);
                    Class<?> type = m.getReturnType();
                    if (type == int.class) {
                        ret = ((Integer) m.invoke(a, new Object[]{}))
                                .compareTo((Integer) m.invoke(b, new Object[]{}));
                    } else if (type == double.class) {
                        ret = ((Double) m.invoke(a, new Object[]{})).compareTo((Double) m
                                .invoke(b, new Object[]{}));
                    } else if (type == long.class) {
                        ret = ((Long) m.invoke(a, new Object[]{})).compareTo((Long) m
                                .invoke(b, new Object[]{}));
                    } else if (type == float.class) {
                        ret = ((Float) m.invoke(a, new Object[]{})).compareTo((Float) m
                                .invoke(b, new Object[]{}));
                    } else if (type == Date.class) {
                        ret = ((Date) m.invoke(a, new Object[]{})).compareTo((Date) m
                                .invoke(b, new Object[]{}));
                    } else if (isImplementsOf(type, Comparable.class)) {
                        ret = ((Comparable) m.invoke(a, new Object[]{}))
                                .compareTo(m.invoke(b, new Object[]{}));
                    } else {
                        ret = String.valueOf(m.invoke(a)).compareTo(
                                String.valueOf(m.invoke(b)));
                    }

                    if (isImplementsOf(type, Comparable.class)) {
                        ret = ((Comparable) m.invoke(a, new Object[]{}))
                                .compareTo(m.invoke(b, new Object[]{}));
                    } else {
                        ret = String.valueOf(m.invoke(a)).compareTo(
                                String.valueOf(m.invoke(b)));
                    }

                } catch (NoSuchMethodException ne) {
                    System.out.println(ne);
                } catch (IllegalAccessException ie) {
                    System.out.println(ie);
                } catch (InvocationTargetException it) {
                    System.out.println(it);
                }

                if (sort != null && sort.toLowerCase().equals(DESC)) {
                    return -ret;
                } else {
                    return ret;
                }
            }
        });
        return list;
    }

    @SuppressWarnings("unchecked")
    public static List<?> sortByMethod(List<?> list, final String[] methods, final String[] sorts) {
        if (methods != null && methods.length > 0) {
            for (int i = methods.length - 1; i >= 0; i--) {
                final String method = methods[i];
                String tmpSort = ASC;
                if (sorts != null && sorts.length > i && sorts[i] != null) {
                    tmpSort = sorts[i];
                }
                final String sort = tmpSort;
                Collections.sort(list, new Comparator() {
                    public int compare(Object a, Object b) {
                        int ret = 0;
                        try {
                            Method m = a.getClass().getMethod(method);
                            m.setAccessible(true);
                            Class<?> type = m.getReturnType();
                            if (type == int.class) {
                                ret = ((Integer) m.invoke(a, new Object[]{}))
                                        .compareTo((Integer) m.invoke(b, new Object[]{}));
                            } else if (type == double.class) {
                                ret = ((Double) m.invoke(a, new Object[]{}))
                                        .compareTo((Double) m.invoke(b, new Object[]{}));
                            } else if (type == long.class) {
                                ret = ((Long) m.invoke(a, new Object[]{}))
                                        .compareTo((Long) m.invoke(b, new Object[]{}));
                            } else if (type == float.class) {
                                ret = ((Float) m.invoke(a, new Object[]{}))
                                        .compareTo((Float) m.invoke(b, new Object[]{}));
                            } else if (type == Date.class) {
                                ret = ((Date) m.invoke(a, new Object[]{}))
                                        .compareTo((Date) m.invoke(b, new Object[]{}));
                            } else if (isImplementsOf(type, Comparable.class)) {
                                ret = ((Comparable) m.invoke(a, new Object[]{}))
                                        .compareTo(m.invoke(b,
                                                new Object[]{}));
                            } else {
                                ret = String.valueOf(m.invoke(a))
                                        .compareTo(
                                                String.valueOf(m
                                                        .invoke(b)));
                            }

                        } catch (NoSuchMethodException ne) {
                            System.out.println(ne);
                        } catch (IllegalAccessException ie) {
                            System.out.println(ie);
                        } catch (InvocationTargetException it) {
                            System.out.println(it);
                        }

                        if (sort != null && sort.toLowerCase().equals(DESC)) {
                            return -ret;
                        } else {
                            return ret;
                        }
                    }
                });
            }
        }
        return list;
    }

    /**
     * 判断对象实现的所有接口中是否包含szInterface
     *
     * @param clazz
     * @param szInterface
     * @return
     */
    public static boolean isImplementsOf(Class<?> clazz, Class<?> szInterface) {
        boolean flag = false;

        Class<?>[] face = clazz.getInterfaces();
        for (Class<?> c : face) {
            if (c == szInterface) {
                flag = true;
            } else {
                flag = isImplementsOf(c, szInterface);
            }
        }

        if (!flag && null != clazz.getSuperclass()) {
            return isImplementsOf(clazz.getSuperclass(), szInterface);
        }

        return flag;
    }


    /**
     * 将list集合，元素随机打乱
     *
     * @param list
     * @param <T>
     */
    public static  <T> void shuffle(List<T> list) {
        int size = list.size();
        Random random = new Random();

        for (int i = 0; i < size; i++) {

            int randomPos = random.nextInt(size);

            Collections.swap(list, i, randomPos);
        }
    }


}