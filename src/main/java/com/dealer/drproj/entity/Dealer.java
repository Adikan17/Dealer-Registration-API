package com.dealer.drproj.entity;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(
    name="dealers",
    indexes={
        @Index(
            name="idx_dealers_type",
            columnList="type"
        )
    }
)
public class Dealer {
    @Id
    @Column(
        length=15,
        nullable=false,
        unique=true
    )
    private String msisdn;

    @Column(
        length=100,
        nullable=false
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
        length=20,
        nullable=false
    )
    private DealerType type;

    @Enumerated(EnumType.STRING)
    @Column(
        length=20,
        nullable=false
    )
    private DealerStatus status;

    @Column(
        nullable=false,
        updatable=false
    )
    private Instant createdAt;

    public Dealer(){}

    @PrePersist
    public void prePersist(){
        if(this.status==null){
            this.status=DealerStatus.ACTIVE;
        }
        this.createdAt=Instant.now();
    }

    public String getMsisdn(){return msisdn;}
    public void setMsisdn(String msisdn){this.msisdn=msisdn;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public DealerType getType(){return type;}
    public void setType(DealerType type){this.type=type;}

    public DealerStatus getStatus(){return status;}
    public void setStatus(DealerStatus status){this.status=status;}

    public Instant getCreatedAt(){return createdAt;}
}
