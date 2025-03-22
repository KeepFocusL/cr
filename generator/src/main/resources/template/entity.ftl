package com.example.cr.${module}.demo;

public class ${className} {
<#list fieldList as field>
    /*
    ${field.comment}
    */
    String ${field.nameLowerCamelCase};

</#list>
}
