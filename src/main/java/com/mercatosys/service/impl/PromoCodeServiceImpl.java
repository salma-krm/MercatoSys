package com.mercatosys.service.impl;

import com.mercatosys.Exception.DuplicateResourceException;
import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.promocode.*;
import com.mercatosys.entity.PromoCode;
import com.mercatosys.mapper.PromoCodeMapper;
import com.mercatosys.repositories.PromoCodeRepository;
import com.mercatosys.service.interfaces.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository repo;
    private final PromoCodeMapper mapper;

    @Override
    public PromoCodeResponseDTO create(PromoCodeRequestDTO dto) {


        if (repo.existsByCodeAndActiveTrue(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Le code promo existe déjà : " + dto.getCode()
            );
        }

        PromoCode promo = mapper.toEntity(dto);
        promo.setActive(true);

        repo.save(promo);
        return mapper.toDTO(promo);
    }

    @Override
    public PromoCodeResponseDTO update(Long id, PromoCodeUpdateDTO dto) {

        PromoCode promo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PromoCode introuvable avec ID = " + id
                ));

        if (dto.getCode() != null &&
                !dto.getCode().equals(promo.getCode()) &&
                repo.existsByCodeAndActiveTrue(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Un autre code promo actif utilise déjà : " + dto.getCode()
            );
        }

        mapper.updateEntityFromDto(dto, promo);
        repo.save(promo);

        return mapper.toDTO(promo);
    }

    @Override
    public PromoCodeResponseDTO getById(Long id) {

        PromoCode promo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PromoCode introuvable avec ID = " + id
                ));

        if (!promo.getActive()) {
            throw new ResourceNotFoundException("Ce code promo est désactivé.");
        }

        return mapper.toDTO(promo);
    }

    @Override
    public List<PromoCodeResponseDTO> getAll() {
        return repo.findAllByActiveTrue()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {

        PromoCode promo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PromoCode introuvable avec ID = " + id
                ));

        promo.setActive(false); // soft delete via active
        repo.save(promo);
    }
}
