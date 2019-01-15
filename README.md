# FixedPattern

快码:前端使用中文显示,而数据库中保存的是对应的英文,所以,快码就是中英文的映射  
同一类型的快码使用一个code表示,如:  
    code:SYS.YES_NO   是: 1   否:0   

本项目主要用于后端接口开发过程中，一个接口,通过不同的type,调用不同的数据获取方式,  
但是，其本身返回给前端的数据格式是固定的,只是具体的数据内容不同的情况下,可以使用该  
框架进行快速开发,减少繁琐的数据格式的构造。  
数据格式如下: 
``` 
    {  
      "msgCode": "10000",  
      "msg": "OK",  
      "content": {  
         "lookUpHeader": [  
              {  
                "code": "FY.PROHIBITION_REASON",  
                "name": "proReason"  
              }  
            ],  
        "header": [  
          {  
            "dataIndex": "cowNum",  
            "title": "牛号",  
            "key": "cowNum"  
          },  
          {  
            "dataIndex": "houseName",  
            "title": "牛舍",  
            "key": "houseName"  
          },  
          {  
             "dataIndex": "proReason",  
             "title": "禁配原因",  
             "key": "proReason"  
           }  
           ......  
        ],  
        "row": {  
          "totalPages": 2,  
          "totalElements": 19,  
          "numberOfElements": 10,  
          "size": 10,  
          "number": 0,  
          "content": [  
            {  
              "cowNum": "12431432432",  
              "houseName": "青年牛舍",  
              "proReason": "breast_prolapse"  
            },  
            {  
               "cowNum": "10010",  
               "houseName": "高产牛舍",  
               "proReason": "breast_prolapse"  
            },  
            ......  
          ],  
          "empty": false  
        }  
      },  
      "status": 1,  
      "total": 3  
    }
``` 
 数据格式如上: 前端使用的是antd的table组件,因此,需要一个表头,header指定了表头中的每一列  
 lookUpHeader指定了该字段中哪些数据需要来源于快码,row是具体的数据内容,该框架可以使用户无需  
 重复编写lookUpHeader和header,只需要关注数据内容就可以.
 
 
 使用教程:  
1. 在application-default.yml中配置包路径:
``` 
        com  
          domain  
            packagePath: xx.xx.xx(具体的包路径) 
``` 
2.配置
```
    @Bean  
    public ArrayClassUtil getArrayClassUtil(Environment env){  
        ArrayClassUtil arrayClassUtil = new ArrayClassUtil();  
        String packagePath = env.getProperty("com.domain.packagePath");  
        arrayClassUtil.setPackagePath(packagePath);
        return arrayClassUtil;
    } 
``` 
3.在配置的包路径下,创建一个实体类
```
    @Type(t_type_xxxx)  
    public Class DemoDomain {  
       
       @Index(index=0,name="属性")  
       private String property;  
       
       @Index(index=1,name="描述")  
       private String desc;  
       
       ......
    }
```
4.新建一个service类，继承AbstractJSONAwareService,并实现getDataStreamByType接口,在该接口中实现定义一个List,实现将该接口的全部type填入其中
```$xslt
      private static final List<String> typeList = new ArrayList<>();
      static {
            typeList.add("t_warn_dryMilkEmpty");
            typeList.add("t_warn_prohibitionLactationCow");
            typeList.add("t_warn_matingOver9NonPregnantLactationCow");
            typeList.add("t_warn_matingOver6NonPregnantYouthCow");
            typeList.add("t_warn_prohibitionYouthCow");
            typeList.add("t_warn_milkVolumeLess15Kg");
            typeList.add("t_warn_lactationDayOver365AdultCow");
            typeList.add("t_warn_over22MonthAgeYouthCow");
            typeList.add("t_warn_matitisOver3LactationCow");
            typeList.add("t_warn_over15MonthAgeNonMatingYouthCow");
            typeList.add("t_warn_pregnantOver225dayLactationCow");
            typeList.add("t_warn_pregnantOver290dayNonCalvingCow");
            typeList.add("t_warn_sickOver30DayNonCureCow");
            typeList.add("t_warn_aborationOver2Cow");
            typeList.add("t_warn_illegalDrugsStorage");
        }
        
        //构造函数
        public xxx() {
            super(typeList)
        }
```

5. 在Controller里面注入DataStreamService,并调用queryDataByType方法
