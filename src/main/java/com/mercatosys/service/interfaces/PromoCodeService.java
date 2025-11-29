package com.mercatosys.service.interfaces;

import com.mercatosys.dto.promocode.PromoCodeRequestDTO;
import com.mercatosys.dto.promocode.PromoCodeResponseDTO;
import com.mercatosys.dto.promocode.PromoCodeUpdateDTO;

import java.util.List;

public interface PromoCodeService {

    PromoCodeResponseDTO create(PromoCodeRequestDTO dto);

    PromoCodeResponseDTO update(Long id, PromoCodeUpdateDTO dto);

    PromoCodeResponseDTO getById(Long id);

    List<PromoCodeResponseDTO> getAll();

    void delete(Long id);
}