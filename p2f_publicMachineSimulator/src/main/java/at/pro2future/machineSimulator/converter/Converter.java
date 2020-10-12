package at.pro2future.machineSimulator.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public interface Converter<From, To, FromFactory, ToFactory> {
	
	From createFrom(To object, FromFactory factory) throws Exception;

	To createTo(From object, ToFactory factory) throws Exception;
	  
   default List<From> createFrom(final Collection<To> objects, FromFactory factory) throws Exception {
       return objects.stream()
               .map(object -> {
					try {
						return createFrom(object, factory);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				})
               .collect(Collectors.toList());
   }

   default List<To> createTo(final Collection<From> objects, ToFactory factory) throws Exception {
       return objects.stream()
               .map(object -> {
					try {
						return createTo(object, factory);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				})
               .collect(Collectors.toList());
   }
}
