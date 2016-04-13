package com.geo.infra.dto;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MappingService {
	
	/**
	 * The method maps the given source object to a target object of the given type.
	 * @param source the source object to map to the target.
	 * @param target the type of the object to map the source object to.
	 * @return created object of the given type, populated with the values mapped from the given source object.
	 */
	public <T> T map(Object source, Class<T> target);
	public <T> void map(Object source, T target);
	
	/**
	 * The method maps the given list of source objects to a list of target objects of the given type.
	 * @param source a list of source objects to map to the target.
	 * @param target the type of the object to map the source object to.
	 * @return created list of objects of the given type, populated with the values mapped from the given list of source objects.
	 */
	public <T,O> List<O> map(List<T> objects, Class<O> target);	
	public <T,O> List<O> map(List<T> objects, Class<O> target, List<O> destination);	
	public <T,O> Set<O> map(Set<T> objects, Class<O> target, Set<O> destination);
	public <T,O> Set<O> map(Set<T> objects, Class<O> target);
	
	/**
	 * The method maps the source Page to a target Page of the given type.
	 * @param page the source page to map to a page of the target type.
	 * @param pageable pagination information associated to the page.
	 * @param target the type of the page to map the source page to.
	 * @return created page of the given type, populated with the values mapped from the given source page.
	 */
	public <T,O> Page<O> map(Page<T> page, Pageable pageable, Class<O> target);
	
}