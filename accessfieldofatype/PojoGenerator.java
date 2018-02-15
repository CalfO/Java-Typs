public class PojoGenerator {
private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws InstanceCreationException, IllegalAccessException, InstantiationException, InvocationTargetException {
		Map<String, List<Object>> injectDataSet  = new HashMap<>();
		PojoGenerator generator = new PojoGenerator(injectDataSet);
		ElasticsearchMessage elasticsearchMessage = generator.generatePojoFed(ElasticsearchMessage.class);
//		String json = mapper.writeValueAsString(elasticsearchMessage);
	}
  }
