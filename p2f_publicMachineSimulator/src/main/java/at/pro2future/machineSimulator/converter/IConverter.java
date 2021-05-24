package at.pro2future.machineSimulator.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This is a generic interface for a converter. It provides two methods for
 * converting the object from one object a another by using the respective factories and vice versa.
 *
 * @param <Source> the first object called from.
 * @param <Target>  the second object called to.
 * @param <SourceFactory> the factory which enables the creation of the from object.
 * @param <TargetFactory> the factory which enables the creation of the object.
 */
public interface IConverter<Source, Target, SourceFactory, TargetFactory> {
    
    /**
     * Creates the source object from the target object by using the given factory.
     * @param object the object which should be converted by using the given factory.
     * @param factory the factory which is able to create the source object.
     * @return the corresponding source object from the given target object.
     * @throws ConvertionNotSupported
     */
    Source createSource(Target object, SourceFactory factory) throws ConvertionNotSupportedException, ConversionFailureException;
    
    /**
     * Creates the target object from the source object by using the given factory.
     * @param object the object which should be converted by using the given factory.
     * @param factory the factory that is able to create the target object.
     * @return a new list where all corresponding source object are converted to the given target object.
     * @throws ConvertionNotSupported
     */
    Target createTarget(Source object, TargetFactory factory) throws ConvertionNotSupportedException, ConversionFailureException;
    
    /**
     * Calls the {@link #createSource}</code> method for a given list of target object.
     * @param objects the object which should be converted.
     * @param factory the factory that is able to create the source object.
     * @return a new list where all corresponding target object are converted to the given source object.
     * @throws ConvertionNotSupported
     */
   default List<Source> createSource(final Collection<Target> objects, SourceFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
       List<Source> sources = new ArrayList<>();
       for(Target object : objects){
           sources.add(createSource(object, factory));
       }
       return sources;
   }
 
   /**
    * Calls the {@link #createTarget}</code> method for a given list of target object.
    * @param objects the object which should be converted.
    * @param factory the factory that is able to create the source object.
    * @return a new list where all corresponding target object are converted to the given source object.
    * @throws ConvertionNotSupported
    */
   default List<Target> createTarget(final Collection<Source> objects, TargetFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
       List<Target> targets = new ArrayList<>();
       for(Source object : objects){
           targets.add(createTarget(object, factory));
       }
       return targets;
   }
}
