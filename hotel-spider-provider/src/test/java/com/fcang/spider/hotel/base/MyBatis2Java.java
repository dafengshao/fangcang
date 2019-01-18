package com.fcang.spider.hotel.base;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  自动生成MyBatis实体映射XML文件、Mapper

 */
@Component
public class MyBatis2Java {
	
	private final String BASE_PATH = "C:/Users/47246/mybatisCoder/domain/";
	

    private final Set<String> tableSet= new HashSet<>(Arrays.asList(
    		"meituan_hotel_info"
    		)
		);
    
    private final Set<String> tablePrefixSet= new HashSet<>(Arrays.asList(
    		"a"
    		)
		);
    private final String author = "hewenfeng";
    
    private final String type_char = "char";
 
    private final String type_date = "date";
    
    private final String type_datetime = "datetime";
 
    private final String type_timestamp = "timestamp";
 
    private final String type_int = "int";
 
    private final String type_bigint = "bigint";
 
    private final String type_text = "text";
 
    private final String type_bit = "bit";
 
    private final String type_decimal = "decimal";
 
    private final String type_blob = "blob";
    private final String BEAN_RELATIVE_PATH = "pojo";
    private final String DAO_RELATIVE_PATH = "mapper";
    private final String IMPL_RELATIVE_PATH = "impl";
    private final String SERVICE_RELATIVE_PATH = "service";
    private final String SQLMAP_RELATIVE_PATH = "sqlmap";




    private final String bean_path = BASE_PATH + BEAN_RELATIVE_PATH;
 
    private final String mapper_path = BASE_PATH + DAO_RELATIVE_PATH;
    
    private final String mapper_impl_path = mapper_path + "/" + IMPL_RELATIVE_PATH;
    
    private final String service_path = BASE_PATH + SERVICE_RELATIVE_PATH;
    
    private final String service_impl_path = service_path + "/" + IMPL_RELATIVE_PATH;
 
    private final String xml_path = BASE_PATH + SQLMAP_RELATIVE_PATH;
 
    private final String bean_package = "com.fcang.spider.hotel.domain.pojo";
 
    private final String mapper_package = "com.fcang.spider.hotel.domain.mapper";
    
    private final String service_package = "com.fcang.spider.hotel.domain.service";
    
    private final String service_package_impl = service_package+"."+IMPL_RELATIVE_PATH;
    
    //private final String mapper_package_impl = "com.frame.dao.impl";
 
 
    private String tableName = null;
 
    private String beanName = null;
 
    private String mapperName = null;
    
    private String mapperNameImpl = null;
    
    private String serviceName = null;
    
    private String serviceNameImpl = null;
 
    
    List<String> beanList = new ArrayList<>();




    /**

     *  获取所有的表

     *

     * @return

     * @throws SQLException

     */
    
    private Connection getConn() {
    	if(conn==null) {
    		conn = sqlSessionFactory.openSession().getConnection();
    	}
    	return conn;
    };
    Connection conn;
    @Autowired
	SqlSessionFactory sqlSessionFactory;
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
		PreparedStatement pstate = getConn().prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString(1);
			if (tableSet.contains(tableName)) {
				tables.add(tableName);
			}

		}
        return tables;
    }


    private void processTable( String table ) {
        StringBuilder sb = new StringBuilder(table.length());
        String tableNew = table.toLowerCase();
        String[] tables = tableNew.split("_");
        String temp = null;
        for ( int i = 0 ; i < tables.length ; i++ ) {
            temp = tables[i].trim();
            if(tablePrefixSet.contains(temp)) {
            	continue;
            }
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        String oneBeanName = sb.toString();
        beanName = oneBeanName+"DO";
        mapperName = beanName + "Mapper";
        //mapperNameImpl = oneBeanName + "DaoImpl";
        serviceName = oneBeanName + "Service";
        serviceNameImpl = oneBeanName + "ServiceImpl";
        beanList.add(beanName);
    }


    private String processType( String type ) {
        if ( type.indexOf(type_char) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_bigint) > -1 ) {
            return "Long";
        } else if ( type.indexOf(type_int) > -1 ) {
            return "Integer";
        } else if ( type.indexOf(type_datetime) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(type_date) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(type_text) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_timestamp) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(type_bit) > -1 ) {
            return "Boolean";
        } else if ( type.indexOf(type_decimal) > -1 ) {
            return "java.math.BigDecimal";
        } else if ( type.indexOf(type_blob) > -1 ) {
            return "byte[]";
        }
        return null;
    }


    private String processField( String field ) {
        StringBuilder sb = new StringBuilder(field.length());
        //field = field.toLowerCase();

        String[] fields = field.split("_");
        String temp = null;
        sb.append(fields[0]);
        for ( int i = 1 ; i < fields.length ; i++ ) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }


    /**

     *  将实体类名首字母改为小写

     *

     * @param beanName

     * @return

     */
    private String processResultMapId( String beanName ) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }


    /**

     *  构建类上面的注释

     *

     * @param bw

     * @param text

     * @return

     * @throws IOException

     */
    private BufferedWriter buildClassComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * @author "+author);
        bw.newLine();
        bw.write(" * @date "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        bw.newLine();
        bw.write(" **/");
        return bw;
    }


    /**

     *  构建方法上面的注释

     *

     * @param bw

     * @param text

     * @return

     * @throws IOException

     */
    private BufferedWriter buildMethodComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t **/");
        return bw;
    }


    /**

     *  生成实体类

     *

     * @param columns

     * @param types

     * @param comments

     * @throws IOException

     */
    private void buildEntityBean( List<String> columns, List<String> types, List<String> comments, String tableComment )
            throws IOException {
        File folder = new File(bean_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File beanFile = new File(bean_path, beanName + ".java");
        if(!beanFile.exists()) {
        	beanFile.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile),"UTF-8"));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.write("import java.io.Serializable;");
        bw.newLine();
        bw.write("import org.apache.commons.lang3.builder.ToStringBuilder;");
        bw.newLine();
        bw.write("import " + bean_package + ".BaseDomain;");
        bw.newLine();
        //bw.write("import lombok.Data;");

        //      bw.write("import javax.persistence.Entity;");

        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        //      bw.write("@Entity");

        //bw.write("@Data");

        //bw.newLine();

        bw.write("public class " + beanName + "  extends BaseDomain  {");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        for ( int i = 0 ; i < size ; i++ ) {
            if("id".equalsIgnoreCase(columns.get(i))){
                continue;
            }

            bw.write("\t/**" + comments.get(i) + "**/");
            bw.newLine();
            bw.write("\tprivate " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
        // 生成get 和 set方法

        String tempField = null;
        String _tempField = null;
        String tempType = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempType = processType(types.get(i));
            _tempField = processField(columns.get(i));
            tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
            if ("id".equalsIgnoreCase(tempField)) {
                continue;
            }
            bw.newLine();
            //          bw.write("\tpublic void set" + tempField + "(" + tempType + " _" + _tempField + "){");
            bw.write("\t/**" + comments.get(i) + "**/");
            bw.newLine();
            bw.write("\tpublic void set" + tempField + "(" + tempType + " " + _tempField + "){");
            bw.newLine();
            //          bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");

            bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            bw.write("\t/**" + comments.get(i) + "**/");
            bw.newLine();
            bw.write("\tpublic " + tempType + " get" + tempField + "(){");
            bw.newLine();
            bw.write("\t\treturn this." + _tempField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
        }
        bw.newLine();
        bw.write("\t@Override");
        bw.newLine();
        bw.write("\tpublic String toString(){");
        bw.newLine();
        bw.write("\t\treturn org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);");
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    private void buildService() throws IOException {
        File folder = new File(service_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperFile = new File(service_path, serviceName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile),"UTF-8"));
        bw.write("package " + service_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + service_package + ".BaseService;");
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.newLine();


        bw = buildClassComment(bw, serviceName + "业务层接口类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");

        bw.write("public interface " + serviceName +  " extends BaseService<" + beanName +" , Long> {");
        bw.newLine();
        bw.newLine();


        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }

    private void buildServiceImpl() throws IOException {
        File folder = new File(service_impl_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperFile = new File(service_impl_path, serviceNameImpl + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile),"UTF-8"));
        bw.write("package " + service_package_impl + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + mapper_package + "." + mapperName + ";");
        bw.newLine();
        bw.write("import " + service_package + "." + serviceName + ";");
        bw.newLine();
        bw.write("import " + mapper_package + ".BaseDao;");
        bw.newLine();
        bw.write("import " + service_package + ".BaseServiceImpl;");
        bw.newLine();
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw.newLine();
        bw.write("import javax.annotation.Resource;");
        bw.newLine();
        bw = buildClassComment(bw, serviceNameImpl + "业务层实现类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");

        bw.write("@Service(\"" + lowerFirsrLetter(serviceName) + "\")");
        bw.newLine();
        bw.write("public class " + serviceNameImpl + " extends BaseServiceImpl<"+ beanName + ", Long> implements "+ serviceName +"{");
        bw.newLine();
        bw.write("\t@Resource");
        bw.newLine();
        bw.write("\tprivate " + mapperName + " "+lowerFirsrLetter(mapperName)+";");
        bw.newLine();
        // ----------定义Mapper中的方法Begin----------

        bw.newLine();
        bw.write("\tpublic "+"BaseDao<"+ beanName + ", Long> getDao(){");
        bw.newLine();
        bw.write("\t\treturn " + lowerFirsrLetter(mapperName) + ";");
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }


    /**

     *  构建Mapper文件

     *

     * @throws IOException

     */
    private void buildDao() throws IOException {
        File folder = new File(mapper_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_path, mapperName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile),"UTF-8"));
        bw.write("package " + mapper_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + mapper_package + ".BaseDao;");
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");

        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");

        bw.write("public interface " + mapperName +  " extends BaseDao<" + beanName +" , Long> {");
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }

    /*private void buildDaoImpl() throws IOException {
        File folder = new File(mapper_impl_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_impl_path, mapperNameImpl + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile),"UTF-8"));
        bw.write("package " + mapper_package_impl + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + mapper_package + ".base.BaseDao;");
        bw.newLine();
        bw.write("import " + mapper_package + ".base.BaseDaoImpl;");
        bw.newLine();
        bw.write("import " + mapper_package + "." + mapperName + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import org.springframework.stereotype.Repository;");
        bw.newLine();
        bw.newLine();
        bw = buildClassComment(bw, mapperNameImpl + "数据库操作实现类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");

        bw.write("@Repository(\"" + lowerFirsrLetter(mapperName) + "\")");
        bw.newLine();
        bw.write("public class " + mapperNameImpl + " extends BaseDaoImpl<" + beanName +", Long> implements "+mapperName+"{");
        bw.newLine();
        bw.write("\tprivate final static String NAMESPACE = \"" + mapper_package + "." + mapperName +".\";");
        bw.newLine();
        bw.newLine();
        bw.newLine();
        bw.write("\t@Override");
        bw.newLine();
        bw.write("\tpublic String getNameSpace(String statement) {\n" +
                "\t\treturn NAMESPACE + statement;\n" +
                "\t}");
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }*/

    /**

     * 首字母小写

     * @return

     */
    private String lowerFirsrLetter(String word){
        return word.substring(0,1).toLowerCase()+word.substring(1);
    }


    /**

     *  构建实体类映射XML文件

     *

     * @param columns

     * @param types

     * @param comments

     * @throws IOException

     */
    private void buildMapperXml( List<String> columns, List<String> types, List<String> comments ) throws IOException {
        File folder = new File(xml_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperXmlFile = new File(xml_path, beanName + "Mapper.xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile),"UTF-8"));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        bw.newLine();
        bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + mapper_package +"."+ mapperName + "\">");
        bw.newLine();
        bw.newLine();

//        bw.write("\t<!--实体映射-->");
//        bw.newLine();

//        bw.write("\t<resultMap id=\"" + this.processResultMapId(beanName) + "ResultMap\" type=\"" + bean_package+"."+beanName + "\">");
//        bw.newLine();
//        int size = columns.size();
//        for ( int i = 0 ; i < size ; i++ ) {
//            bw.write("\t\t<result property=\""
//                    + this.processField(columns.get(i)) + "\" column=\"" + columns.get(i) + "\" />");
//            bw.newLine();
//        }
//        bw.write("\t</resultMap>");

        bw.newLine();

        // 下面开始写SqlMapper中的方法

        // this.outputSqlMapperMethod(bw, columns, types);

        buildSQL(bw, columns, types);

        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }


    private void buildSQL( BufferedWriter bw, List<String> columns, List<String> types ) throws IOException {
        int size = columns.size();
        // 通用结果列

        bw.write("\t<!-- 通用查询结果列-->");
        bw.newLine();
        bw.write("\t<!-- 所有查询列 -->");
        bw.newLine();
        bw.write("\t<sql id=\"QUERY_COLUMN_LIST\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[");

        for ( int i = 0 ; i < size ; i++ ) {
            bw.newLine();
            bw.write("\t\t\t"+ columns.get(i));
            if(columns.get(i).contains("_")){
                bw.write(" AS " + processField(columns.get(i)));
            }
            if ( i != size - 1 ) {
                bw.write(",");
            }
        }
        bw.newLine();
        bw.write("\t\t]]>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
        bw.write("\t<!-- 查询列来源表 -->");
        bw.newLine();
        bw.write("\t<sql id=\"QUERY_FROM_TABLE\"><![CDATA[FROM "+ tableName  + "]]></sql>");
        bw.newLine();
        bw.newLine();
        bw.newLine();

        bw.newLine();
        bw.write("\t<!--全部条件(更多功能可以通过queryData扩展实现) -->");
        bw.newLine();
        bw.write("\t<sql id=\"QUERY_WHERE_CLAUSE\">");
        bw.newLine();
        bw.write("\t\t<where>");
        bw.newLine();
        for ( int i = 0 ; i < size ; i++ ) {
//            if("java.util.Date".equals(processType(types.get(i)))){
//                bw.write("\t\t<if test=\"startDate!=null\">");
//                bw.write("and " + columns.get(i)+" >= #{startDate}</if>");
//                bw.newLine();
//                bw.write("\t\t<if test=\"endDate!=null\">");
//                bw.write("and " + columns.get(i)+" &lt;= #{endDate}</if>");
//            }else{
                bw.write("\t\t\t<if test=\""+processField(columns.get(i))+"!=null \">");
                bw.write("<![CDATA[AND " + columns.get(i)+"= #{"+processField(columns.get(i))+"}]]></if>");
//            }
            bw.newLine();
        }
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");

        bw.newLine();
       
        bw.write("\t<!-- 更新列字段,只要不为NULL则更新,除开主键列 -->");
        // 修改update方法
        bw.write("\t<!-- 修 改-->");
        bw.newLine();
        bw.write("\t<sql id=\"UPDATE_COLUMN_SET\">");
        bw.newLine();
        bw.write("\t\t<set>");
        bw.newLine();
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t\t\t<if test=\""+processField(columns.get(i))+"!=null\">");
            bw.write("<![CDATA[" +columns.get(i)+" = #{"+processField(columns.get(i))+"},]]></if>");
            bw.newLine();
        }
        bw.write("\t\t</set>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();


        // 添加insert方法

        bw.write("\t<!-- 插入记录 -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertSelective\" parameterType=\"" + processResultMapId(beanName) + "\" useGeneratedKeys=\"true\" keyProperty=\""+columns.get(0)+"\">");
        bw.newLine();
      
        bw.write("\t\t\tINSERT INTO " + tableName);
        bw.newLine();
      
        bw.write("\t\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for ( int i = 1 ; i < size ; i++ ) {
        	bw.write("\t\t\t\t<if test=\""+processField(columns.get(i))+"!=null\">");
            bw.write(columns.get(i));
            bw.write(",</if>");
            bw.newLine();
        }
      
        bw.write("\t\t\t</trim>");
        bw.newLine();
        bw.write("\t\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for ( int i = 1 ; i < size ; i++ ) {
        	bw.write("\t\t\t\t<if test=\""+processField(columns.get(i))+"!=null\">");
            bw.write("#{" + processField(columns.get(i)) + "},</if>");
            bw.newLine();
        }
        bw.write("\t\t\t</trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // 添加insert完

        bw.write("\t<!-- 返回插入的编号,在事务开启状态下有效 -->");
        bw.newLine();
        bw.write("\t<select id=\"lastSequence\" resultType=\"int\"><![CDATA[SELECT LAST_INSERT_ID() AS id]]></select>");
        bw.newLine();
        bw.newLine();

        bw.write("\t<!-- 删除记录,主键IN(array)  -->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteByArrayKey\" parameterType=\"java.lang.reflect.Array\">");
        bw.newLine();
        bw.write("\t\t <![CDATA[DELETE FROM " + tableName + "  WHERE id IN]]>");
        bw.newLine();
        bw.write("\t\t <foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        bw.newLine();
        bw.write("\t\t\t <![CDATA[#{id}]]>");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完


        bw.write("\t<!--删除,通过条件-->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteByCondtion\" parameterType=\""+ processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[DELETE FROM " + tableName +"]]>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"QUERY_WHERE_CLAUSE\"/>");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

        bw.write("\t<!--删除,通过主键-->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteByPrimaryKey\" parameterType=\""+ processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t <![CDATA[DELETE FROM " + tableName + "  WHERE id = #{id}]]>");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

        bw.write("\t<!--修改记录通过主键-->");
        bw.newLine();
        bw.write("\t<update id=\"updateByPrimaryKeySelective\" parameterType=\""+ processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[UPDATE " + tableName +"]]>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"UPDATE_COLUMN_SET\"/>");
        bw.newLine();
        bw.write("\t\t\t<![CDATA[WHERE id = #{id}]]>");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update done


        bw.write("\t<!-- 查询,通过主键==(key) -->");
        bw.newLine();
        bw.write("\t<select id=\"selectByPrimaryKey\" parameterType=\"java.lang.Long\" resultType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[SELECT]]>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_COLUMN_LIST\"/>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_FROM_TABLE\"/>");
        bw.newLine();
        bw.write("\t\t<![CDATA[WHERE id =#{id}]]>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        
        bw.write("\t<!-- 查询,通过主键IN(array) -->");
        bw.newLine();
        bw.write("\t<select id=\"selectEntryArray\" parameterType=\"java.lang.reflect.Array\" resultType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[SELECT]]>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_COLUMN_LIST\"/>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_FROM_TABLE\"/>");
        bw.newLine();
        bw.write("\t\t<![CDATA[WHERE id IN]]>");
        bw.newLine();
        bw.write("\t\t<foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        bw.newLine();
        bw.write("\t\t\t<![CDATA[#{id}]]>");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();

        bw.write("\t<!-- 查询,通过条件 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectEntryList\" parameterType=\"" + processResultMapId(beanName) + "\" resultType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[SELECT]]>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_COLUMN_LIST\"/>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_FROM_TABLE\"/>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_WHERE_CLAUSE\"/>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        

        bw.write("\t<!-- 总数查询,通过条件-->");
        bw.newLine();
        bw.write("\t<select id=\"selectEntryListCount\" parameterType=\"" + processResultMapId(beanName) + "\" resultType=\"int\">");
        bw.newLine();
        bw.write("\t\t<![CDATA[SELECT COUNT(id) AS dataCount]]>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_FROM_TABLE\"/>");
        bw.newLine();
        bw.write("\t\t<include refid=\"QUERY_WHERE_CLAUSE\"/>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完


        bw.newLine();
        bw.newLine();
    }


    /**

     *  获取所有的数据库表注释

     *

     * @return

     * @throws SQLException

     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = getConn().prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while ( results.next() ) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }


    public void generate() throws ClassNotFoundException, SQLException, IOException {
        String prefix = "show full fields from ";
        List<String> columns = null;
        List<String> types = null;
        List<String> comments = null;
        PreparedStatement pstate = null;
        List<String> tables = getTables();
        Map<String, String> tableComments = getTableComment();
        for ( String table : tables ) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            pstate = getConn().prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while ( results.next() ) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTable(table);
            //          this.outputBaseBean();

            String tableComment = tableComments.get(tableName);
            buildEntityBean(columns, types, comments, tableComment);
            buildDao();
            //buildDaoImpl();
            buildService();
            buildServiceImpl();
            buildMapperXml(columns, types, comments);
        }
        buildMapperConfigXML();
        getConn().close();
    }

    private void buildMapperConfigXML() throws IOException{
        File folder = new File(xml_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        File mapperXmlFile = new File(xml_path, "sqlmap-config.xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile),"UTF-8"));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD  Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">");
        bw.newLine();
        bw.write("<configuration>");
        bw.newLine();
        bw.write("\t<settings>");
        bw.newLine();
        bw.write("\t\t<!-- 全局映射器启用缓存 -->");
        bw.newLine();
        bw.write("\t\t<setting name=\"cacheEnabled\" value=\"true\" />");
        bw.newLine();
        bw.write("\t\t<!-- 对于未知的SQL查询，允许返回不同的结果集以达到通用的效果 -->");
        bw.newLine();
        bw.write("\t\t<setting name=\"multipleResultSetsEnabled\" value=\"true\" />");
        bw.newLine();
        bw.write("\t\t<!-- 允许使用列标签代替列名 -->");
        bw.newLine();
        bw.write("\t\t<setting name=\"useColumnLabel\" value=\"true\" />");
        bw.newLine();
        bw.write("\t\t<!-- 数据库超过30秒仍未响应则超时 -->");
        bw.newLine();
        bw.write("\t\t<setting name=\"defaultStatementTimeout\" value=\"30\" />");
        bw.newLine();
        bw.write("\t\t<!-- 启用下划线与驼峰式命名规则的映射 -->");
        bw.newLine();
        bw.write("\t\t<setting name=\"mapUnderscoreToCamelCase\" value=\"true\" />");
        bw.newLine();
        bw.write("\t</settings>");
        bw.newLine();
        bw.write("\t<typeAliases>");
        bw.newLine();
        for (String bean : beanList) {
            bw.write("\t\t<typeAlias type=\""+bean_package+"."+bean+"\" alias=\""+lowerFirsrLetter(bean)+"\"/>");
            bw.newLine();
        }
        bw.write("\t</typeAliases>");
        bw.newLine();
        bw.write("\t<mappers>");
        bw.newLine();
        for (String bean : beanList) {
            bw.write("\t\t<mapper resource=\"" + SQLMAP_RELATIVE_PATH + "/" +bean + ".xml\" />");
            bw.newLine();
        }
        bw.write("\t</mappers>");
        bw.newLine();
        bw.write("</configuration>");
        bw.newLine();
        bw.flush();
        bw.close();
    }


    public void run( ) {
        try {
            generate();
            // 自动打开生成文件的目录

//            Runtime.getRuntime().exec("cmd /c start explorer D:\\");
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        } catch ( SQLException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
