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

        if (repo.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Le code promo existe déjà : " + dto.getCode());
        }

        PromoCode promo = mapper.toEntity(dto);
        repo.save(promo);

        return mapper.toDTO(promo);
    }

    @Override
    public PromoCodeResponseDTO update(Long id, PromoCodeUpdateDTO dto) {

        PromoCode promo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromoCode introuvable avec ID = " + id));

        // Vérifier unicité du code SI le code change
        if (dto.getCode() != null &&
                !dto.getCode().equals(promo.getCode()) &&
                repo.existsByCode(dto.getCode())) {

            throw new IllegalArgumentException("Un autre code promo existe déjà avec le code : " + dto.getCode());
        }

        mapper.updateEntityFromDto(dto, promo);
        repo.save(promo);

        return mapper.toDTO(promo);
    }

    @Override
    public PromoCodeResponseDTO getById(Long id) {
        PromoCode promo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromoCode introuvable avec ID = " + id));

        return mapper.toDTO(promo);
    }

    @Override
    public List<PromoCodeResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("PromoCode introuvable avec ID = " + id);
        }
        repo.deleteById(id);
    }
}
