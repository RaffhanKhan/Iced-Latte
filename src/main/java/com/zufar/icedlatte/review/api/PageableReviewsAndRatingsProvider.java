package com.zufar.icedlatte.review.api;

import com.zufar.icedlatte.openapi.dto.ProductReviewWithRating;
import com.zufar.icedlatte.openapi.dto.ProductReviewsAndRatingsWithPagination;
import com.zufar.icedlatte.review.converter.ProductReviewDtoConverter;
import com.zufar.icedlatte.review.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.zufar.icedlatte.common.util.Utils.createPageableObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageableReviewsAndRatingsProvider {

    private final ProductReviewRepository reviewRepository;
    private final ProductReviewDtoConverter productReviewDtoConverter;
    private final ProductReviewValidator productReviewValidator;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductReviewsAndRatingsWithPagination getProductReviews(final UUID productId,
                                                                    final Integer page,
                                                                    final Integer size,
                                                                    final String sortAttribute,
                                                                    final String sortDirection) {
        productReviewValidator.validateProductExists(productId);
        Pageable pageable = createPageableObject(page, size, sortAttribute, sortDirection);
        Page<ProductReviewWithRating> productReviewWithRatingPage = reviewRepository.findByProductIdWithRatings(productId, pageable);
        return productReviewDtoConverter.toProductReviewsAndRatingsWithPagination(productReviewWithRatingPage);
    }

}