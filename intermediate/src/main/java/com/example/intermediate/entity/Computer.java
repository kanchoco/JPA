/*
* 컴퓨터(Computer)
   해상도(Screen)
   브랜드(Brand)
   상품명(Name)
   가격(Price)
   출시일(ReleaseDate)
   램(Ram)
   SSD
   GPU
   Processor
   등록일(CreatedDate)
   수정일(UpdatedDate)

데스크탑(Desktop)
   키보드타입(Keyboard)

핸드폰(Phone)
   배터리용량(Battery)
*
* */
package com.example.intermediate.entity;

import com.example.intermediate.type.Hardware;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ELECTRONIC_DEVICE")
@Table(name = "TBL_COMPUTER")
@Getter @Setter @ToString
@NoArgsConstructor
public class Computer extends Period{
    @Id @GeneratedValue
    private Long computerId;
    private int computerScreen;
    private String computerBrand;
    private String computerName;
    private int computerPrice;
    private LocalDateTime computerReleaseDate;
    @Embedded // 모듈을 사용할 때 작성한다.
    private Hardware hardware;

    public void create(int computerScreen, String computerBrand, String computerName, int computerPrice, LocalDateTime computerReleaseDate, Hardware hardware) {
        this.computerScreen = computerScreen;
        this.computerBrand = computerBrand;
        this.computerName = computerName;
        this.computerPrice = computerPrice;
        this.computerReleaseDate = computerReleaseDate;
        this.hardware = hardware;
    }
}





















