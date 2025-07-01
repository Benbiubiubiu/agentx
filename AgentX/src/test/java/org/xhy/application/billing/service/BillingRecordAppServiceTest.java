package org.xhy.application.billing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xhy.application.billing.dto.BillingStatisticsDTO;
import org.xhy.domain.billing.model.dto.BillingStatistics;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.interfaces.dto.billing.GetBillingStatisticsRequest;
import org.xhy.infrastructure.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * 账单记录应用服务测试
 */
@ExtendWith(MockitoExtension.class)
class BillingRecordAppServiceTest {

    @Mock
    private BillingRecordDomainService billingRecordDomainService;

    @InjectMocks
    private BillingRecordAppService billingRecordAppService;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GetBillingStatisticsRequest request;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now().minusDays(30);
        endTime = LocalDateTime.now();
        request = new GetBillingStatisticsRequest(startTime, endTime);
    }

    @Test
    void testGetBillingStatistics_Success() {
        // Arrange
        String userId = "test-user-id";
        BillingStatistics mockStatistics = new BillingStatistics(
            BigDecimal.valueOf(100.50),
            BigDecimal.valueOf(50.25),
            BigDecimal.valueOf(10.00),
            10L,
            5L,
            1L,
            BigDecimal.valueOf(10.05),
            BigDecimal.valueOf(20.00),
            BigDecimal.valueOf(5.00),
            startTime,
            endTime
        );

        when(billingRecordDomainService.getBillingStatistics(
            eq(userId), 
            eq(startTime), 
            eq(endTime), 
            isNull(), 
            isNull()
        )).thenReturn(mockStatistics);

        // Act
        BillingStatisticsDTO result = billingRecordAppService.getBillingStatistics(userId, request);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.50), result.getTotalAmount());
        assertEquals(BigDecimal.valueOf(50.25), result.getMonthlyAmount());
        assertEquals(BigDecimal.valueOf(10.00), result.getTodayAmount());
        assertEquals(10L, result.getTotalCount());
        assertEquals(5L, result.getMonthlyCount());
        assertEquals(1L, result.getTodayCount());
        assertEquals(BigDecimal.valueOf(10.05), result.getAverageAmount());
        assertEquals(BigDecimal.valueOf(20.00), result.getMaxAmount());
        assertEquals(BigDecimal.valueOf(5.00), result.getMinAmount());
    }

    @Test
    void testGetBillingStatistics_WithProductId() {
        // Arrange
        String userId = "test-user-id";
        String productId = "test-product-id";
        request.setProductId(productId);

        BillingStatistics mockStatistics = new BillingStatistics(
            BigDecimal.valueOf(50.00),
            BigDecimal.valueOf(25.00),
            BigDecimal.valueOf(5.00),
            5L,
            3L,
            1L,
            BigDecimal.valueOf(10.00),
            BigDecimal.valueOf(15.00),
            BigDecimal.valueOf(5.00),
            startTime,
            endTime
        );

        when(billingRecordDomainService.getBillingStatistics(
            eq(userId), 
            eq(startTime), 
            eq(endTime), 
            eq(productId), 
            isNull()
        )).thenReturn(mockStatistics);

        // Act
        BillingStatisticsDTO result = billingRecordAppService.getBillingStatistics(userId, request);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50.00), result.getTotalAmount());
        assertEquals(5L, result.getTotalCount());
    }

    @Test
    void testGetBillingStatistics_InvalidTimeRange() {
        // Arrange
        String userId = "test-user-id";
        request.setStartTime(endTime);
        request.setEndTime(startTime);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingRecordAppService.getBillingStatistics(userId, request);
        });
        assertEquals("开始时间不能晚于结束时间", exception.getMessage());
    }

    @Test
    void testGetBillingStatistics_TimeRangeTooLong() {
        // Arrange
        String userId = "test-user-id";
        request.setStartTime(LocalDateTime.now().minusYears(2));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingRecordAppService.getBillingStatistics(userId, request);
        });
        assertEquals("查询时间范围不能超过一年", exception.getMessage());
    }

    @Test
    void testGetBillingStatistics_NullStartTime() {
        // Arrange
        String userId = "test-user-id";
        request.setStartTime(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingRecordAppService.getBillingStatistics(userId, request);
        });
        assertEquals("开始时间和结束时间不能为空", exception.getMessage());
    }

    @Test
    void testGetBillingStatistics_NullEndTime() {
        // Arrange
        String userId = "test-user-id";
        request.setEndTime(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingRecordAppService.getBillingStatistics(userId, request);
        });
        assertEquals("开始时间和结束时间不能为空", exception.getMessage());
    }
} 