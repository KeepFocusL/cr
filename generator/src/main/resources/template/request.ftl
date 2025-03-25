package com.example.cr.${module}.request;

<#list typeSet as type>
  <#if type == 'Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
  </#if>
  <#if type == 'BigDecimal'>
import java.math.BigDecimal;
  </#if>
</#list>
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ${Domain}Request {
    <#list fieldList as field>
    /**
     * ${field.nameCn}
     */
      <#if field.javaType == 'Date'>
        <#if field.type=='time'>
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type=='date'>
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
        <#else>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        </#if>
      </#if>
      <#if field.nameLowerCamelCase=='idCard'>
    @Size(min = 18, max = 18, message = "【${field.nameCn}】必须是18位")
      </#if>
      <#if field.nameLowerCamelCase == 'mobile'>
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "【${field.nameCn}】格式不正确")
      </#if>
      <#if field.name != "id" && field.nameLowerCamelCase != "userId" && field.nameLowerCamelCase != "createTime" && field.nameLowerCamelCase != "updateTime">
        <#if !field.nullAble>
          <#if field.javaType == 'String'>
    @NotBlank(message = "【${field.nameCn}】不能为空")
          <#else>
    @NotNull(message = "【${field.nameCn}】不能为空")
          </#if>
        </#if>
        <#if field.length gt 0>
    @Size(max = ${field.length})
        </#if>
      </#if>
    private ${field.javaType} ${field.nameLowerCamelCase};

    </#list>

    <#list fieldList as field>
    public ${field.javaType} get${field.nameUpperCamelCase}() {
        return ${field.nameLowerCamelCase};
    }

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