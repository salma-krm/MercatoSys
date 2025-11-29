package com.mercatosys.service.interfaces;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;

/**
 * Interface du service de paiement.
 * Supporte les paiements fractionnés pour une commande.
 */
public interface PaymentService {

    /**
     * Effectuer un paiement pour une commande donnée.
     * Peut être une partie du montant total (paiement fractionné).
     *
     * @param orderId ID de la commande
     * @param dto Informations sur le paiement (montant, moyen, référence, banque...)
     * @return PaymentResponseDTO contenant les détails du paiement enregistré
     * @throws IllegalArgumentException si le montant dépasse le restant dû ou est invalide
     * @throws ResourceNotFoundException si la commande n'existe pas
     */
    PaymentResponseDTO payOrder(Long orderId, PaymentRequestDTO dto);
}
