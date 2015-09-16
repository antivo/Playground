package com.drmtx.service;

import com.drmtx.frequency.Frequency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by antivo on 8/27/15.
 */
@Repository
public interface FrequencyService extends CrudRepository<Frequency, Long> {
    /**
     * Save frequency into the repository
     * @param frequency frequency to be stored
     * @return frequency object stored
     */
    @Override
    Frequency save(Frequency frequency);

    /**
     * Find frequency by the id parameter in the repository
     * @param id id of the search frequency
     * @return frequency
     */
    @Override
    Frequency findOne(Long id);
}
