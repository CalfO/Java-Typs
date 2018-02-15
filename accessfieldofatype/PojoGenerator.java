public class PojoGenerator {
private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws InstanceCreationException, IllegalAccessException, InstantiationException, InvocationTargetException {
		Map<String, List<Object>> injectDataSet  = new HashMap<>();
		PojoFeeder feeder = new PojoFeeder(injectDataSet);
		WhatEverPojo myPojoFed = feeder.generatePojoFed(WhatEverPojo.class);
//		String json = mapper.writeValueAsString(myPojoFed);
	}
  }
