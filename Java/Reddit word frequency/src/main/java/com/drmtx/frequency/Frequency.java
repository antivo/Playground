package com.drmtx.frequency;



import javax.persistence.*;
import java.util.Map;

/**
 * Created by antivo on 8/27/15.
 */

/**
 * Represents word frequencies for one comment.
 */
@Entity
public class Frequency {
    protected Frequency() {}

    public Frequency(Map<String, Long> frequency) {
        this.frequency = frequency;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="words")
    @Column(name="frequency")
    @CollectionTable(name="attributes", joinColumns=@JoinColumn(name="keys"))
    private Map<String, Long> frequency;

    public Map<String, Long> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<String, Long> frequency) {
        this.frequency = frequency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Frequency{" +
                "frequency=" + frequency +
                ", id=" + id +
                '}';
    }
}
