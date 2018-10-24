package com.jwcjlu.gateway.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PluginExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PluginExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPluginNameIsNull() {
            addCriterion("plugin_name is null");
            return (Criteria) this;
        }

        public Criteria andPluginNameIsNotNull() {
            addCriterion("plugin_name is not null");
            return (Criteria) this;
        }

        public Criteria andPluginNameEqualTo(String value) {
            addCriterion("plugin_name =", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameNotEqualTo(String value) {
            addCriterion("plugin_name <>", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameGreaterThan(String value) {
            addCriterion("plugin_name >", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameGreaterThanOrEqualTo(String value) {
            addCriterion("plugin_name >=", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameLessThan(String value) {
            addCriterion("plugin_name <", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameLessThanOrEqualTo(String value) {
            addCriterion("plugin_name <=", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameLike(String value) {
            addCriterion("plugin_name like", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameNotLike(String value) {
            addCriterion("plugin_name not like", value, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameIn(List<String> values) {
            addCriterion("plugin_name in", values, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameNotIn(List<String> values) {
            addCriterion("plugin_name not in", values, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameBetween(String value1, String value2) {
            addCriterion("plugin_name between", value1, value2, "pluginName");
            return (Criteria) this;
        }

        public Criteria andPluginNameNotBetween(String value1, String value2) {
            addCriterion("plugin_name not between", value1, value2, "pluginName");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNull() {
            addCriterion("enabled is null");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNotNull() {
            addCriterion("enabled is not null");
            return (Criteria) this;
        }

        public Criteria andEnabledEqualTo(Byte value) {
            addCriterion("enabled =", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotEqualTo(Byte value) {
            addCriterion("enabled <>", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThan(Byte value) {
            addCriterion("enabled >", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThanOrEqualTo(Byte value) {
            addCriterion("enabled >=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThan(Byte value) {
            addCriterion("enabled <", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThanOrEqualTo(Byte value) {
            addCriterion("enabled <=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledIn(List<Byte> values) {
            addCriterion("enabled in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotIn(List<Byte> values) {
            addCriterion("enabled not in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledBetween(Byte value1, Byte value2) {
            addCriterion("enabled between", value1, value2, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotBetween(Byte value1, Byte value2) {
            addCriterion("enabled not between", value1, value2, "enabled");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedIsNull() {
            addCriterion("time_created is null");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedIsNotNull() {
            addCriterion("time_created is not null");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedEqualTo(Date value) {
            addCriterion("time_created =", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedNotEqualTo(Date value) {
            addCriterion("time_created <>", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedGreaterThan(Date value) {
            addCriterion("time_created >", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("time_created >=", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedLessThan(Date value) {
            addCriterion("time_created <", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedLessThanOrEqualTo(Date value) {
            addCriterion("time_created <=", value, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedIn(List<Date> values) {
            addCriterion("time_created in", values, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedNotIn(List<Date> values) {
            addCriterion("time_created not in", values, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedBetween(Date value1, Date value2) {
            addCriterion("time_created between", value1, value2, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeCreatedNotBetween(Date value1, Date value2) {
            addCriterion("time_created not between", value1, value2, "timeCreated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedIsNull() {
            addCriterion("time_updated is null");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedIsNotNull() {
            addCriterion("time_updated is not null");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedEqualTo(Date value) {
            addCriterion("time_updated =", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedNotEqualTo(Date value) {
            addCriterion("time_updated <>", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedGreaterThan(Date value) {
            addCriterion("time_updated >", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("time_updated >=", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedLessThan(Date value) {
            addCriterion("time_updated <", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("time_updated <=", value, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedIn(List<Date> values) {
            addCriterion("time_updated in", values, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedNotIn(List<Date> values) {
            addCriterion("time_updated not in", values, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedBetween(Date value1, Date value2) {
            addCriterion("time_updated between", value1, value2, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andTimeUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("time_updated not between", value1, value2, "timeUpdated");
            return (Criteria) this;
        }

        public Criteria andPluginNameLikeInsensitive(String value) {
            addCriterion("upper(plugin_name) like", value.toUpperCase(), "pluginName");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}