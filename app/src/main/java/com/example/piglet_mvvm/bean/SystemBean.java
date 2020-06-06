package com.example.piglet_mvvm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/2/22
 */
public class SystemBean implements Serializable {
    /**
     * children : [
     ** children : [
     *
     *      ]
     *      courseId : 13
     *      id : 408
     *      name : 鸿洋
     *      order : 190000
     *      parentChapterId : 407
     *      userControlSetTop : false
     *      visible : 1
     * ]
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */

    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private boolean userControlSetTop;
    private int visible;
    private List<SystemBean> children;


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public boolean isUserControlSetTop() {
        return userControlSetTop;
    }

    public void setUserControlSetTop(boolean userControlSetTop) {
        this.userControlSetTop = userControlSetTop;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<SystemBean> getChildren() {
        return children;
    }

    public void setChildren(List<SystemBean> children) {
        this.children = children;
    }
}
