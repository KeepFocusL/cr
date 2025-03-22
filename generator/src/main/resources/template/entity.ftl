package com.example.cr.${module}.demo;

import java.util.Date;

public class ${className} {
<#list fieldList as field>
    /*
    ${field.comment}
    */
    ${field.javaType} ${field.nameLowerCamelCase};

</#list>
}
