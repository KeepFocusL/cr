package com.example.cr.${module}.service;

import com.example.cr.${module}.entity.${Domain};
import com.example.cr.${module}.mapper.${Domain}Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ${Domain}Service {
private static final Logger log = LoggerFactory.getLogger(${Domain}Service.class);

@Autowired
${Domain}Mapper ${domain}Mapper;

List<${Domain}> list(){
List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(null);
return ${domain}List;
}
}
