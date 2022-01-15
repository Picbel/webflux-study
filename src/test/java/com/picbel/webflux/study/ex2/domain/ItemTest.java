package com.picbel.webflux.study.ex2.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

  @Test
  void itemBasicsShouldWork() throws Exception {
      //given
      Item sampleItem = new Item("item1","TV tray", "Alf TV tray", 19.99);
      //when
      assertThat(sampleItem.getId()).isEqualTo("item1");
      //then
  }

}