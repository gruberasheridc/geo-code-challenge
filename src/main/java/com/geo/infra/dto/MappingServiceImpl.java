package com.geo.infra.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class MappingServiceImpl implements MappingService {

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
    public <T> T map(Object source, Class<T> target) {
        return mapper.map(source, target);
    }

	@Override
    public <T> void map(Object source, T target) {
        mapper.map(source, target);
    }

	@Override
	public <T,O> List<O> map(List<T> objects, Class<O> target) {
        return (List<O>) internalMap(objects, target, new ArrayList<O>());
    }

	@Override
    public <T,O> List<O> map(List<T> objects, Class<O> target, List<O> destination) {
        return (List<O>) internalMap(objects, target, destination);
    }

	@Override
    public <T,O> Set<O> map(Set<T> objects, Class<O> target, Set<O> destination) {
        return (Set<O>) internalMap(objects, target, destination);
    }

	@Override
	public <T,O> Set<O> map(Set<T> objects, Class<O> target) {
        return map(objects, target, new HashSet<O>());
    }
	
	@Override
	public <T,O> Page<O> map(Page<T> page, Pageable pageable, Class<O> target) {
		List<O> content = map(page.getContent(), target);
		Page<O> mapedPaged = new PageImpl<O>(content, pageable, page.getTotalElements());
        return mapedPaged;
    }

    private <T, O> Collection<O> internalMap(Collection<T> objects, Class<O> target, Collection<O> destination) {
        for (T t : objects) {
            destination.add(mapper.map(t, target));
        }
        
        return destination;
    }

}