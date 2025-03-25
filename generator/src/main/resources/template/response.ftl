package com.example.cr.${module}.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
<#list typeSet as type>
  <#if type=='Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
  </#if>
  <#if type=='BigDecimal'>
import java.math.BigDecimal;
  </#if>
</#list>

public class ${Domain}Response {
    <#list fieldList as field>
    /**
     * ${field.nameCn}
     */
      <#if field.javaType == 'Date'>
        <#if field.type == 'time'>
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'date'>
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
        <#else>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        </#if>
      </#if>
      <#if field.name == 'id' || field.name?ends_with('_id')>
    @JsonSerialize(using = ToStringSerializer.class)
      </#if>
    private ${field.javaType} ${field.nameLowerCamelCase};

    </#list>

    <#list fieldList as field>
      <#if field.name == 'id_card' || field.name == 'password'>
    public ${field.javaType} get${field.nameUpperCamelCase}() {
        if (${field.nameLowerCamelCase} == null || ${field.nameLowerCamelCase}.length() < 8) {
            return ${field.nameLowerCamelCase};
        }
        // 保留前4位和后4位，中间用 * 代替
        return ${field.nameLowerCamelCase}.substring(0, 4) +
                "*".repeat(${field.nameLowerCamelCase}.length() - 8) +
                ${field.nameLowerCamelCase}.substring(${field.nameLowerCamelCase}.length() - 4);
    }
      <#else>
    public ${field.javaType} get${field.nameUpperCamelCase}() {
        return ${field.nameLowerCamelCase};
    }
      </#if>

    public void set${field.nameUpperCamelCase}(${field.javaType} ${field.nameLowerCamelCase}) {
        this.${field.nameLowerCamelCase} = ${field.nameLowerCamelCase};
    }

    </#list>
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        <#list fieldList as field>
        sb.append(", ${field.nameLowerCamelCase}=").append(${field.nameLowerCamelCase});
        </#list>
        sb.append("]");
        return sb.toString();
    }
}