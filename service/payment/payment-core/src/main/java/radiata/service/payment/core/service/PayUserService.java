package radiata.service.payment.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.payment.dto.request.PayUserAddMoneyRequestDto;
import radiata.common.domain.payment.dto.request.PayUserCreateRequestDto;
import radiata.common.domain.payment.dto.request.PayUserSubtractMoneyRequestDto;
import radiata.common.domain.payment.dto.response.PayUserResponseDto;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.model.vo.Money;
import radiata.service.payment.core.implementation.PayUserReader;
import radiata.service.payment.core.implementation.PayUserSaver;
import radiata.service.payment.core.implementation.PayUserValidator;
import radiata.service.payment.core.service.mapper.PayUserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayUserService {

    private final PayUserSaver payUserSaver;
    private final PayUserReader payUserReader;
    private final PayUserMapper payUserMapper;
    private final PayUserValidator payUserValidator;

    /**
     * 간편결제 사용자 조회
     */
    @Transactional(readOnly = true)
    public PayUserResponseDto getPayUser(String userId) {
        PayUser payUser = payUserReader.getPayUserByUserId(userId);
        return payUserMapper.toPayUserResponseDto(payUser);
    }

    /**
     * 간편결제 사용자 등록
     */
    @Transactional
    public PayUserResponseDto signupPayUser(String userId, PayUserCreateRequestDto request) {
        PayUser payUser = payUserSaver.createPayUser(userId, request.password());
        return payUserMapper.toPayUserResponseDto(payUser);
    }

    /**
     * 간편결제 사용자 잔액 추가
     */
    @Transactional
    public PayUserResponseDto addMoney(String userId, PayUserAddMoneyRequestDto request) {
        PayUser payUser = payUserReader.getPayUserByUserId(userId);
        payUserValidator.validatePassword(payUser.getPassword(), request.password());
        payUser.deposit(Money.of(request.amount()));
        return payUserMapper.toPayUserResponseDto(payUser);
    }

    /**
     * 간편결제 사용자 잔액 차감
     */
    @Transactional
    public PayUserResponseDto subtractMoney(String userId, PayUserSubtractMoneyRequestDto request) {
        PayUser payUser = payUserReader.getPayUserByUserId(userId);
        payUserValidator.validatePassword(payUser.getPassword(), request.password());
        payUser.withdraw(Money.of(request.amount()));
        return payUserMapper.toPayUserResponseDto(payUser);
    }
}
