package jumpstart.util;

public class ClassUtil {

	static public String extractUnqualifiedName(Object obj) {
		Class<? extends Object> cls = obj.getClass();
		return extractUnqualifiedName(cls);
	}

	static public String extractUnqualifiedName(Class<? extends Object> cls) {
		String s = cls.getName();
		String[] s1 = s.split("\\.");
		return s1[s1.length - 1];
	}

}