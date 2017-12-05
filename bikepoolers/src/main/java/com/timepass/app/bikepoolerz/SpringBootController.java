package com.timepass.app.bikepoolerz;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timepass.app.bikepoolerz.utilities.HibernateConnection;

@Controller
@EnableAutoConfiguration
@ComponentScan
public class SpringBootController {

	/**
	 * @param args
	 */
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private HibernateProperties hibernateProperties;
	
	
	/*HibernateProperties hibernateProperties;
	
	

	public HibernateProperties getHibernateProperties() {
		return hibernateProperties;
	}

	@Autowired
	public void setHibernateProperties(HibernateProperties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}*/

	public HibernateProperties getHibernateProperties() {
		return hibernateProperties;
	}

	public void setHibernateProperties(HibernateProperties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}

	public AppProperties getAppProperties() {
		return appProperties;
	}
	
	public void setAppProperties(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		SpringApplication.run(SpringBootController.class, args);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@RequestMapping("/someOtherMapping")
	@ResponseBody
	String home() {
		
        //String globalProperties = global.toString();

        //logger.debug("Welcome {}, {}", app, global);
	//	if(null == hibernateProperties){
			//System.out.println("properties is null");
	//	}else{
			//Set keyset = properties.getHibernateProperties().keySet();
			//for(Object key : keyset){
			//	System.out.println("Object : "+ .getProperties() +" Value  : "/*+ properties.getHibernateProperties().get(key)*/);
			
			//}
			Map<String, Object> hibProps = new HashMap<String, Object>();
			if(null == hibernateProperties){
				System.out.println("hibernate is null");
			}else{
				hibProps = hibernateProperties.getHibernateProperties();
				Set<String> keyset = hibProps.keySet();
				for(String key : keyset){
					System.out.println("Key :" + key +" Value : "+ hibProps.get(key));
				}
			}
			if(null == appProperties)
				System.out.println("app is null");
			else{
				System.out.println(appProperties.toString());
			}
	//	}
			HibernateConnection conn  = new HibernateConnection();
			conn.setHibernateProperties(hibernateProperties);
			SessionFactory sess = conn.doConnection();
			Session session = sess.getCurrentSession();
			Transaction tx = session.beginTransaction();
		//	Criteria cr = session.createCriteria(Category.class);
			//List result =  cr.list();
			/*Iterator<Category> it = result.iterator();
			while(it.hasNext()){
				Category cat = it.next();
				System.out.println(cat.getCategoryName());
			}*/
			//model.put("message", appProperties + globalProperties);
        return ObjectUtils.nullSafeToString(appProperties);
	}
	
	@RequestMapping("/category")
	@ResponseBody
	public String getCategory() {
		//JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:test2",
         //       "username",
          //      "password");
		//DBI dbi = establishConnection(hibernateProperties.getConnection().getUrl(), hibernateProperties.getConnection().getUsername(), hibernateProperties.getConnection().getPassword());
		BasicDataSource ds = new BasicDataSource();
		//ds.addConnectionProperty(name, value)
		DBI dbi = new DBI(hibernateProperties.getConnection().getUrl());
    
        return ObjectUtils.nullSafeToString(appProperties);
	}
	
	@RequestMapping("/check_queries")
	@ResponseBody
	public String getCheckQueries() {
		loadEntitiesMap();
		return "Queries conversion completed";
	}
	
	private void loadEntitiesMap(){
		entitiesPath = "F:\\WorkSpace\\SpringBoot_Hibernate\\springboot\\src\\main\\java\\com\\learn\\springboot\\entities\\";
		File entitiesDir = new File(entitiesPath);
		File[] entities = entitiesDir.listFiles();
		entitiesMapObject = new HashMap<String, Object>();
		for(File entity : entities){
			loadSingleEntityIntoObject(entity);
		}
		Set<String> keys = entitiesMapObject.keySet();
		for(String key : keys){
			HashMap<String, Object> map = (HashMap<String, Object>) entitiesMapObject.get(key);
			Set<String> colKeys = map.keySet();
			for(String colKey : colKeys){
				System.out.println("Entity Table Name : "+ key+" Column : "+ colKey+" Value : "+map.get(colKey));
			}
		}
		
		daoPath = "F:\\WorkSpace\\SpringBoot_Hibernate\\springboot\\src\\main\\java\\com\\learn\\springboot\\dao\\";
		File daoDir = new File(daoPath);
		File[] daoFiles = daoDir.listFiles();
	//	entitiesMapObject = new HashMap<String, Object>();
	
		quriesMapObject.clear();
		
		for(File daoFile : daoFiles){
			loadSqlQueriesIntoObject(daoFile);
		}
		Set<String> queriesKeys = quriesMapObject.keySet();
		
		for(String key : queriesKeys){
			HashMap<String, Object> map = (HashMap<String, Object>) quriesMapObject.get(key);
			Set<String> colKeys = map.keySet();
			for(String colKey : colKeys){
				loadHqlQueries(key, colKey, ""+map.get(colKey));
				System.out.println("DAO Class Name : "+ key+" Method : "+ colKey+" query : "+map.get(colKey));
			}
		}
		
		if(null != hqlQuriesMapObject){
			Set<String> hqlkeys = hqlQuriesMapObject.keySet();
			for(String hqlkey : hqlkeys){
				System.out.println(hqlQuriesMapObject.get(hqlkey));
			}
		}
		
	}
	
	private void loadHqlQueries(String key, String colKey, String query) {
		// TODO Auto-generated method stub
		try{
			String querySplit[] =  query.split(" ");
			String tables = "";
			boolean isFrom = false;
			boolean isWhere = true;
			String hqlQuery = query;
			LinkedList<String> queryLL = new LinkedList<String>();
			//queryLL = parseQueryForSpace(queryLL, query);
			
			queryLL = parseQueryForComma(queryLL, query);

			queryLL = parseQueryForEquals(queryLL);
			
			queryLL = parseQueryForBracket(queryLL);
			
			queryLL = parseQueryForColon(queryLL);

			queryLL = parseQueryForSpace(queryLL, query);
			
			List<String> tablesList = new ArrayList<String>();
			boolean start = false;

			for(String tbl : queryLL){
				if("from".equalsIgnoreCase(tbl)){
					start = true;
				}else if("where".equalsIgnoreCase(tbl) ){
					start = false;
				}else{
					if(start){
						if(!tbl.trim().equalsIgnoreCase("") && !tbl.trim().equalsIgnoreCase(",") )
							tablesList.add(tbl.toLowerCase());
					}
				}
			}
			
			boolean isPartOne = true;
			boolean isPartTwo = false;
			boolean isPartThree = false;
			boolean isPartFour = false;
			boolean isPartFive = false;
			
			LinkedList<String> hqlQueryList =  new LinkedList<String>();
			int count =0 ;
			for(String str : queryLL){
				if(isPartOne){
					if(str.equalsIgnoreCase("from")){
						isPartOne = false;
						isPartTwo = true; 
						hqlQueryList.add(count++,"FROM");
					}else {
						if(!str.equalsIgnoreCase("SELECT") && !str.equalsIgnoreCase("*") ){
							//getImmediateUsefulContext();
							String convStr  = convertThePartOne(str, tablesList);
							hqlQueryList.add(count++ , convStr);
						}else{
							hqlQueryList.add(count++ , str);
						}
					}
				}else if(isPartTwo){
					if(str.equalsIgnoreCase("where")){
						isPartTwo = false;
						isPartThree = true; 
						hqlQueryList.add(count++,"WHERE");
					}else {
						if(!str.equalsIgnoreCase("FROM") && !str.equalsIgnoreCase("*") ){
							//getImmediateUsefulContext();
							String convStr  = convertThePartTWO(str, tablesList);
							hqlQueryList.add(count++ , convStr);
						}else{
							hqlQueryList.add(count++ , str);
						}
					}
				}else if (isPartThree){
					if(str.equalsIgnoreCase("order")){
						isPartThree = false;
						isPartFour = true; 
					}else {
						if(!str.equalsIgnoreCase("WHERE") && !str.equalsIgnoreCase("*") ){
							//getImmediateUsefulContext();
							String convStr  = convertThePartTHREE(str, tablesList);
							hqlQueryList.add(count++ , convStr);
						}else{
							hqlQueryList.add(count++ , str);
						}
					}
				}else if( isPartFour){
					
				}else if(isPartFive) {
					
				}
				
			}
			
			/*for(String split : queryLL){
				
				HashMap<String, Object> entityObject = (HashMap<String, Object>) entitiesMapObject.get(split.trim().toLowerCase());
				Set<String> keys = entityObject.keySet();
				for( String key1 : keys){
					 hqlQuery.replace(key1 ,""+entityObject.get(key));
					 System.out.println(hqlQuery);

				}
			}*/
					
			System.out.println("Tables" + tables);
			String convertedHqlQuery = "";
			for (Iterator iterator = hqlQueryList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				convertedHqlQuery = convertedHqlQuery + string;
				System.out.println(string);
			}
			hqlQuriesMapObject.put(colKey, convertedHqlQuery);
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
		
	}

	private String convertThePartTHREE(String str, List<String> tablesList) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> conversionList = new ArrayList<HashMap<String, Object>>();
		Set<String> keys = entitiesMapObject.keySet();
		HashMap<String,Object> conversionMap = new HashMap<String, Object>();
		for(String key : keys){
			if(tablesList.contains(key)){
				conversionMap.putAll((Map<? extends String, ? extends Object>) entitiesMapObject.get(key));
				conversionList.add(conversionMap);
			}
		}
		String str1 ="";
		String convertedValue = "";
		if(!str.trim().isEmpty() && !str.trim().contains(",") && !str.contains(":") && !str.equalsIgnoreCase("AND")
				&& !str.equalsIgnoreCase("OR") && !str.equalsIgnoreCase("=") && !str.equalsIgnoreCase("in")){
			str1 = str.trim().toUpperCase();
			for(HashMap<String, Object> map : conversionList){
				if(map.containsKey(str1)){
					convertedValue = String.valueOf(map.get(str1));
					break;
				}else{
					
				}
			}
		}else{
			convertedValue = str;
		}
		return convertedValue;
	}

	private String convertThePartTWO(String str, List<String> tablesList) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> conversionList = new ArrayList<HashMap<String, Object>>();
		Set<String> keys = entitiesMapObject.keySet();
		if(str.equalsIgnoreCase("food_items")){
			System.out.println("Its for food item");
			
		}
		String str1 ="";
		String convertedValue = "";
		HashMap<String,Object> conversionMap = new HashMap<String, Object>();
		if(!str.trim().isEmpty() && !str.trim().contains(",")){
			for(String key : keys){
				if(tablesList.contains(key)){
					conversionMap = new HashMap<String, Object>();
					conversionMap.putAll((Map<? extends String, ? extends Object>) entitiesMapObject.get(key));
					conversionList.add(conversionMap);
					if(str.equalsIgnoreCase(key)){
						
							String fileName  = String.valueOf(conversionMap.get("ENTITY_OBJECT_NAME"));
							convertedValue = fileName;
							break;
					}
				}
			}
		}else {
			convertedValue = str;
		}
		
		
		return convertedValue;
	}

	private String convertThePartOne(String str, List<String> tablesList) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> conversionList = new ArrayList<HashMap<String, Object>>();
		Set<String> keys = entitiesMapObject.keySet();
		HashMap<String,Object> conversionMap = new HashMap<String, Object>();
		for(String key : keys){
			if(tablesList.contains(key)){
				conversionMap.putAll((Map<? extends String, ? extends Object>) entitiesMapObject.get(key));
				conversionList.add(conversionMap);
			}
		}
		for(String table : tablesList){
			
			System.out.println("tables used are" + table);
		}
		String str1 ="";
		String convertedValue = "";
		if(!str.trim().isEmpty() && !str.trim().contains(",")){
			str1 = str.trim().toUpperCase();
			for(HashMap<String, Object> map : conversionList){
				if(map.containsKey(str1)){
					convertedValue = String.valueOf(map.get(str1));
					break;
				}else{
					
				}
			}
		}else{
			convertedValue = str;
		}
		return convertedValue;
	}

	private LinkedList<String> parseQueryForColon(LinkedList<String> queryLL) {
		// TODO Auto-generated method stub
		return queryLL;
	}

	private LinkedList<String> parseQueryForBracket(LinkedList<String> queryLL) {
		// TODO Auto-generated method stub
		return queryLL;
	}

	private LinkedList<String> parseQueryForEquals(LinkedList<String> queryLL) {
		// TODO Auto-generated method stub
		LinkedList<String> newList = new LinkedList<String>();
		int count = 0;
		for(String str : queryLL){
			
			if(str.contains("=")){
				String split[] = str.split("=");
				newList.add(count++, split[0]);
				newList.add(count++, "=");
				newList.add(count++, split[1]);
			}else{
				newList.add(count++, str);
			}
			
		}
		
		for(String str : newList){
			System.out.println(str);
		}
		return newList;
	}

	private LinkedList<String> parseQueryForComma(LinkedList<String> queryLL, String query) {
		// TODO Auto-generated method stub
		LinkedList<String> newList = new LinkedList<String>();
		int count = 0;

				String split[] = query.split(",");
				for(int i= 0; i< split.length ; i++){
					String temp = split[i];
					newList.add(count++, temp);
					if( (i+1) != split.length ){
						newList.add(count++, ", ");
					}
				}			
		
		
		for(String str : newList){
			System.out.println(str);
		}
		return newList;
	}

	private LinkedList<String> parseQueryForSpace(LinkedList<String> queryLL, String query) {
		// TODO Auto-generated method stub
		LinkedList<String> newList = new LinkedList<String>();
		int count = 0;

				
				for(int i= 0; i< queryLL.size() ; i++){
					String str = queryLL.get(i);
					String split[] = str.split(" ");
					for(int j= 0; j< split.length ; j++){
						String temp = split[j];
						newList.add(count++, temp);
	//					if( (i+1) != split.length ){
							newList.add(count++, " ");
		//				}
					}		
				}			
		
		
				for(String str : newList){
					System.out.println(str);
				}
		return newList;
	}

	private void loadSqlQueriesIntoObject(File daoFile) {
		// TODO Auto-generated method stub
		try{
			String fileName = "com.learn.springboot.dao."+daoFile.getName().replace(".java", "");
			Class<?> daoClass = Class.forName(fileName);;
			Method methods[] = daoClass.getDeclaredMethods();
			
			
			
			Map<String, Object> queryObjectMap = new HashMap<String, Object>();
			for(Method method : methods){
				Annotation annotations[] = method.getAnnotations();
				for(Annotation annotation : annotations){
					String annotationName = annotation.annotationType().getName();
					System.out.println("Annotation name"+ annotationName);
					Object value =  new Object();
					String methodName = "";
					if(annotationName.contains("org.skife.jdbi.v2.sqlobject.SqlQuery")){
						Method declaredMethods[] = annotation.annotationType().getDeclaredMethods();
						for (Method declaredMethod : declaredMethods) {
			            	value = declaredMethod.invoke(annotation, (Object[])null);
			                System.out.println(" " + method.getName() + ": " + value);
			                methodName = method.getName();
			                if(methodName.equals("name")){
			                	break;
			                }
			            }
						queryObjectMap.put(method.getName(), ""+value);
					}else if(annotationName.contains("org.skife.jdbi.v2.sqlobject.SqlUpdate")){
						
					}else {
						
					}
					
				}
			}
			quriesMapObject.put(daoFile.getName().replace(".java", ""), queryObjectMap);
			
 		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	private void loadSingleEntityIntoObject(File entity) {
		// TODO Auto-generated method stub
		try{
			Class entityClass = Class.forName("com.learn.springboot.entities."+entity.getName().replace(".java", ""));
			Field[] fields = entityClass.getDeclaredFields();
			HashMap<String, Object> entityMap = new HashMap<String, Object>();
			Annotation classAnnotations[] = entityClass.getAnnotations();
			String tableName = "";
			
		//Annotation scanning for getting the table name	
			for(Annotation clasAnnotation : classAnnotations ){
				String annotationName = "" + clasAnnotation.annotationType();
				Method methods[] = clasAnnotation.annotationType().getDeclaredMethods();
				if(annotationName.contains("javax.persistence.Table")){
					Object value = "";  
					String methodName = "";
					for (Method method : methods) {
		            	value = method.invoke(clasAnnotation, (Object[])null);
		                System.out.println(" " + method.getName() + ": " + value);
		                methodName = method.getName();
		                if(methodName.equals("name")){
		                	break;
		                }
		            }
					/*isColumn = true;
					System.out.println(" is Column :"+ isColumn + " Value "+ value);*/
					//entityMap.put(value.toString(), fieldName);
					tableName = value.toString().toLowerCase();
				}
			}
		//Annotation scanning for getting the table name
		//Fields scanning for loading them to objects	
			for(Field field : fields){
					String fieldName = field.getName();
					String fieldType = ""+field.getType();
					System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
				
					if(fieldType.contains("java.") || field.getType().isPrimitive()){//Simple Column
						Annotation[] annotations = field.getAnnotations();
						for(Annotation annotation : annotations){
							System.out.println("annotation : " + annotation.annotationType());
							Method methods[] = annotation.annotationType().getDeclaredMethods();
							String annotationName = "" + annotation.annotationType();
							if(annotationName.contains("javax.persistence.Column")){
									Object value = "";  
									String methodName = "";
									for (Method method : methods) {
						            	value = method.invoke(annotation, (Object[])null);
						                methodName = method.getName();
						                if(methodName.equals("name")){
						                	break;
						                }
						            }
									entityMap.put(value.toString(), fieldName);
							}
						}
					}else { // field is a composite or an another entity or unused field
						Annotation[] annotations = field.getAnnotations();
						for(Annotation annotation : annotations){
							System.out.println("annotation : " + annotation.annotationType());
							String annotationName = "" + annotation.annotationType();
							if(annotationName.contains("javax.persistence.JoinColumn")){
								Object value = "";  
								String methodName = "";
								String fieldNameEntity = loadAnotherEntityObject(field);
								Method methods[] = annotation.annotationType().getDeclaredMethods();
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								entityMap.put(value.toString(), fieldName+"."+fieldNameEntity);
							}else if(annotationName.contains("javax.persistence.EmbeddedId")){
								loadCompositeEntityObject(field, entityMap );
							}
						}
					}		
			}
			entityMap.put("ENTITY_OBJECT_NAME", entity.getName().replace(".java", ""));
			entitiesMapObject.put(tableName, entityMap);
		//Fields scanning for loading them to objects
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	private String loadAnotherEntityObject(Field fieldEntity) {
		// TODO Auto-generated method stub
		try{
			
			String className = getClassNameFromField(fieldEntity);
			Class<?> entityClass = Class.forName(className);
			Field[] fields = entityClass.getDeclaredFields();
			String columnName = getColumnName(fieldEntity);
			for(Field field : fields){
				String fieldName = field.getName();
				String fieldType = ""+field.getType();
				System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
			
				if(fieldType.contains("java.") || field.getType().isPrimitive()){//Simple Column
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						Method methods[] = annotation.annotationType().getDeclaredMethods();
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.Column")){
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								if(columnName.equalsIgnoreCase(""+value)){
									return fieldName;
								}
								//entityMap.put(value.toString(), fieldName);
						}
					}
				}else { // field is a composite or an another entity or unused field
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.JoinColumn")){
							Object value = "";  
							String methodName = "";
							String fieldNameEntity = loadAnotherEntityObject(field);
							Method methods[] = annotation.annotationType().getDeclaredMethods();
							for (Method method : methods) {
				            	value = method.invoke(annotation, (Object[])null);
				                methodName = method.getName();
				                if(methodName.equals("name")){
				                	break;
				                }
				            }
							if(columnName.equalsIgnoreCase(""+value)){
								return fieldName+"."+fieldNameEntity;
							}
						}else if(annotationName.contains("javax.persistence.EmbeddedId")){
							return searchCompositeEntityObject(field, columnName);
						}
					}
				}		
		}
			
		}catch(Exception ex){
			System.out.println("Exception caught in load composite object for field "+ fieldEntity.getName());
			ex.printStackTrace();
		}

		
		return "";
	}

	private String searchCompositeEntityObject(Field fieldEntity, String columnName) {
		// TODO Auto-generated method stub
		try{
			String className = getClassNameFromField(fieldEntity);
			Class<?> entityClass = Class.forName(className);
			Field[] fields = entityClass.getDeclaredFields();
			
			for(Field field : fields){
				String fieldName = field.getName();
				String fieldType = ""+field.getType();
				System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
			
				if(fieldType.contains("java.") || field.getType().isPrimitive()){//Simple Column
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						Method methods[] = annotation.annotationType().getDeclaredMethods();
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.Column")){
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								if(columnName.equals(""+value)){
									return fieldName;
								}
								//entityMap.put(value.toString(), fieldName);
						}
					}
				} else { //Since composite cannot have another composite inside it.. so assuming an another entity or unusable field
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.JoinColumn")){
							String valueFromAnotherEntity = loadAnotherEntityObject(field);
							Method methods[] = annotation.annotationType().getDeclaredMethods();
							Object value = "";  
							String methodName = "";
							for (Method method : methods) {
				            	value = method.invoke(annotation, (Object[])null);
				                methodName = method.getName();
				                if(methodName.equals("name")){
				                	break;
				                }
				            }
							if(columnName.equals(""+value)){
								return fieldName+"."+valueFromAnotherEntity;
							}
						}else { // Since composite and another is eliminated discarding any other 
						
						}
					}
				}
			}
			
		}catch(Exception ex){
			System.out.println("Exception caught in load composite object for field "+ fieldEntity.getName());
			ex.printStackTrace();
		}
		return "";
	}

	private String getColumnName(Field fieldEntity) {
		// TODO Auto-generated method stub
		String methodName = "";
		Object value = new Object();
		try{
			Annotation[] annotations = fieldEntity.getAnnotations();
			for(Annotation annotation : annotations){
				String annotationName = "" + annotation.annotationType();
				if(annotationName.contains("javax.persistence.JoinColumn") ||
						annotationName.contains("javax.persistence.Column")){
					Method methods[] = annotation.annotationType().getDeclaredMethods();
					for (Method method : methods) {
		            	value = method.invoke(annotation, (Object[])null);
		                methodName = method.getName();
		                if(methodName.equals("name")){
		                	break;
		                }
		            }
				}
			}
		}catch(Exception ex){
			System.out.println("Exception caught while handling ");
			ex.printStackTrace();
		}
		
		return value.toString();
	}

	private void loadCompositeEntityObject(Field fieldEntity,
			HashMap<String, Object> entityMap) {
		// TODO Auto-generated method stub
		try{
			String className = getClassNameFromField(fieldEntity);
			Class<?> entityClass = Class.forName(className);
			Field[] fields = entityClass.getDeclaredFields();
			
			for(Field field : fields){
				String fieldName = field.getName();
				String fieldType = ""+field.getType();
				System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
			
				if(fieldType.contains("java.") || field.getType().isPrimitive()){//Simple Column
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						Method methods[] = annotation.annotationType().getDeclaredMethods();
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.Column")){
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								entityMap.put(value.toString(), fieldEntity.getName()+"."+fieldName);
						}
					}
				} else { //Since composite cannot have another composite inside it.. so assuming an another entity or unusable field
					Annotation[] annotations = field.getAnnotations();
					for(Annotation annotation : annotations){
						System.out.println("annotation : " + annotation.annotationType());
						String annotationName = "" + annotation.annotationType();
						if(annotationName.contains("javax.persistence.JoinColumn")){
							String valueFromAnotherEntity = loadAnotherEntityObject(field);
							Method methods[] = annotation.annotationType().getDeclaredMethods();
							Object value = "";  
							String methodName = "";
							for (Method method : methods) {
				            	value = method.invoke(annotation, (Object[])null);
				                methodName = method.getName();
				                if(methodName.equals("name")){
				                	break;
				                }
				            }
							entityMap.put(value.toString(),  fieldEntity.getName()+"."+fieldName+"."+valueFromAnotherEntity);
						}else { // Since composite and another is eliminated discarding any other 
						
						}
					}
				}
			}
			
		}catch(Exception ex){
			System.out.println("Exception caught in load composite object for field "+ fieldEntity.getName());
			ex.printStackTrace();
		}
	}
		

	private String getClassNameFromField(Field field) {
		// TODO Auto-generated method stub
		String className = "";
		try{
			className =( ""+ field.getType()).replace("class ", "");
		}catch(Exception ex){
			System.out.println("Exception caught in get class name from fields "+ field.getName());
			ex.printStackTrace();
		}
		
		return className;
	}

	private void loadCompositeEntityObject(String fieldTypeEntity,
			HashMap<String, Object> entityMap, String fieldNameEntity, String methodNameEntity, String tableNameEntity) {
		// TODO Auto-generated method stub
		try{
			
			Class entityClass = Class.forName(fieldTypeEntity.replace("class ", ""));
			Field[] fields = entityClass.getDeclaredFields();
			//HashMap<String, Object> entityMap = new HashMap<String, Object>();
			for(Field field : fields){
				String fieldName = field.getName();
				String fieldType = ""+field.getType();
				System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
				Annotation[] annotations = field.getAnnotations();
				for(Annotation annotation : annotations){
					System.out.println("annotation : " + annotation.annotationType());
					Method methods[] = annotation.annotationType().getDeclaredMethods();
					String annotationName = "" + annotation.annotationType();
					boolean isColumn = false;
					boolean isEmbedded = false;
					boolean isAnotherEntity = false;
					if(fieldType.contains("java.") || field.getType().isPrimitive()){
						if(annotationName.contains("javax.persistence.Column")){
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                System.out.println(" " + method.getName() + ": " + value);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								isColumn = true;
								System.out.println(" is Column :"+ isColumn + " Value "+ value);
								entityMap.put(value.toString(), fieldNameEntity+"."+fieldName);
						}else if(annotationName.contains("javax.persistence.JoinColumn")){
								System.out.println(" is Column :"+ isColumn + " Value "+ "");
								isAnotherEntity = true;
						}else if(annotationName.contains("javax.persistence.EmbeddedId")){
								isEmbedded = true;
						}else {
							
						}
						if(isAnotherEntity){
							//loadAnotherEntityObject(fieldType, entityMap, fieldName);
						}
						if(isEmbedded){
							//loadCompositeEntityObject(fieldType, entityMap, fieldName );
						}
						
						System.out.println("Is a primitive Field" + isColumn);
					}	
				}
		/*		Annotation classAnnotations[] = entityClass.getAnnotations();
				String tableName = "";
				for(Annotation clasAnnotation : classAnnotations ){
					String annotationName = "" + clasAnnotation.annotationType();
					Method methods[] = clasAnnotation.annotationType().getDeclaredMethods();
					if(annotationName.contains("javax.persistence.Table")){
						Object value = "";  
						String methodName = "";
						for (Method method : methods) {
			            	value = method.invoke(clasAnnotation, (Object[])null);
			                System.out.println(" " + method.getName() + ": " + value);
			                methodName = method.getName();
			                if(methodName.equals("name")){
			                	break;
			                }
			            }
						isColumn = true;
						System.out.println(" is Column :"+ isColumn + " Value "+ value);
						//entityMap.put(value.toString(), fieldName);
						tableName = value.toString();
					}
				}
				entitiesMapObject.put(tableNameEntity, entityMap);	*/
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	private void loadAnotherEntityObject(String fieldTypeEntity,
			HashMap<String, Object> entityMap, String fieldNameEntity, String columnNameEntity, String tableNameEntity) {
		// TODO Auto-generated method stub
try{
			
			Class entityClass = Class.forName(fieldTypeEntity.replace("class ", ""));
			Field[] fields = entityClass.getDeclaredFields();
			//HashMap<String, Object> entityMap = new HashMap<String, Object>();
			for(Field field : fields){
				String fieldName = field.getName();
				String fieldType = ""+field.getType();
				System.out.println("Field Name : "+ fieldName +" Field type :"+ fieldType);
				Annotation[] annotations = field.getAnnotations();
				for(Annotation annotation : annotations){
					System.out.println("annotation : " + annotation.annotationType());
					Method methods[] = annotation.annotationType().getDeclaredMethods();
					String annotationName = "" + annotation.annotationType();
					boolean isColumn = false;
					boolean isEmbedded = false;
					boolean isAnotherEntity = false;
					if(fieldType.contains("java.") || field.getType().isPrimitive()){
						if(annotationName.contains("javax.persistence.Column")){
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                System.out.println(" " + method.getName() + ": " + value);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								isColumn = true;
								System.out.println(" is Column :"+ isColumn + " Value "+ value);
								entityMap.put(value.toString(), fieldNameEntity+"."+fieldName);
						}else if(annotationName.contains("javax.persistence.JoinColumn")){
								System.out.println(" is Column :"+ isColumn + " Value "+ "");
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                System.out.println(" " + method.getName() + ": " + value);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								isColumn = true;
								System.out.println(" is Column :"+ isColumn + " Value "+ value);
								isAnotherEntity = true;
								loadAnotherEntityObject(fieldType, entityMap, fieldName, methodName, tableNameEntity);
						}else if(annotationName.contains("javax.persistence.EmbeddedId")){
								isEmbedded = true;
								Object value = "";  
								String methodName = "";
								for (Method method : methods) {
					            	value = method.invoke(annotation, (Object[])null);
					                System.out.println(" " + method.getName() + ": " + value);
					                methodName = method.getName();
					                if(methodName.equals("name")){
					                	break;
					                }
					            }
								loadCompositeEntityObject(fieldType, entityMap, fieldName, methodName, tableNameEntity );
						}else {
							
						}
						if(isAnotherEntity){
							
						}
						if(isEmbedded){
							
						}
						
						System.out.println("Is a primitive Field" + isColumn);
					}	
				}
				Annotation classAnnotations[] = entityClass.getAnnotations();
				String tableName = "";
				for(Annotation clasAnnotation : classAnnotations ){
					String annotationName = "" + clasAnnotation.annotationType();
					Method methods[] = clasAnnotation.annotationType().getDeclaredMethods();
					if(annotationName.contains("javax.persistence.Table")){
						Object value = "";  
						String methodName = "";
						for (Method method : methods) {
			            	value = method.invoke(clasAnnotation, (Object[])null);
			                System.out.println(" " + method.getName() + ": " + value);
			                methodName = method.getName();
			                if(methodName.equals("name")){
			                	break;
			                }
			            }
						/*isColumn = true;
						System.out.println(" is Column :"+ isColumn + " Value "+ value);*/
						//entityMap.put(value.toString(), fieldName);
						tableName = value.toString().toLowerCase();
					}
				}
				entitiesMapObject.put(tableName, entityMap);	
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		
	}

	private static String entitiesPath = "";
	private static Map<String,Object> entitiesMapObject = new HashMap<String, Object>();

	private static String daoPath = "";
	private static Map<String,Object> quriesMapObject = new HashMap<String, Object>();
	
	private static Map<String,Object> hqlQuriesMapObject = new HashMap<String, Object>();
}
//TODO:

/***
 *	 I Would require the Class name to map it to correct table entity -- DONE
 *	 Also i would require the column name to run a check -- DONE
 *   Need to get Immediate Context for checking 
 *   Get the next context and process them all as a one unit and add it to the query
 * 
 * 
 * 
 * */
 