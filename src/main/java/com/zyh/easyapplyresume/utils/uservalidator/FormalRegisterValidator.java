package com.zyh.easyapplyresume.utils.uservalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;

import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * FormalRegisterForm检查工具类，用于验证用户注册表单数据并设置默认值
 * 核心规则：
 * - 必填项：用户账号、用户名称、用户密码
 * - 选填项：邮箱、手机号、头像、简介、目标岗位、薪资、工作数、福利、地址、大学编码（均有默认值）
 * @author shiningCloud2025
 */
public class FormalRegisterValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    private static final int ACCOUNT_MIN_LENGTH = 7;
    private static final int ACCOUNT_MAX_LENGTH = 10;
    private static final int USERNAME_MIN_LENGTH = 1;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int EMAIL_MAX_LENGTH = 25;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 30;
    private static final int IMAGE_MAX_LENGTH = 255;
    private static final int INTRODUCE_MAX_LENGTH = 200;
    private static final int GOOD_WELFARE_MAX_LENGTH = 200;
    private static final BigDecimal MAX_SALARY_LIMIT = new BigDecimal("100000");
    private static final int MIN_WEEK_WORK_DAY = 0;
    private static final int MAX_WEEK_WORK_DAY = 7;
    private static final int LOCATION_DETAIL_MAX_LENGTH = 255;
    private static final String DEFAULT_IMAGE = "https://ts4.tc.mm.bing.net/th/id/OIP-C.sPOk8TwPGgwtB2SU6ngYUgAAAA?rs=1&pid=ImgDetMain&o=7&rm=3";
    private static final String DEFAULT_INTRODUCE = "用户很懒，尚未填写简介。";
    private static final String DEFAULT_EMAIL_PREFIX = "未填写邮箱";
    private static final String DEFAULT_PHONE_PREFIX = "未填写手机号";
    private static final Random RANDOM = new Random();
    private static final String DEFAULT_GOOD_WELFARE = "用户很懒，还没有写希望的福利待遇";
    private static final String DEFAULT_LOCATION_DETAIL = "用户很懒，还没有写详细地址";

    /**
     * 普通注册校验规则：
     * - 强制必填：用户账号（7-10位）、用户名称（1-20位）、用户密码（6-30位）
     * - 选填字段：邮箱（默认"未填写邮箱+20位随机数"）、手机（默认"未填写手机号+5位随机数"）、头像、简介、目标岗位、薪资范围（0-100000）、工作天数（0-7）、福利、地址、大学编码（均设置默认值）
     */
    public static void validateForRegister(FormalRegisterForm form) {
        // 1. 强制必填：用户账号（非空+长度7-10位）→ USER_ACCOUNT_EMPTY(10003) / USER_ACCOUNT_LENGTH_ERROR(10004)
        if (form.getUserAccount() == null || form.getUserAccount().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_EMPTY);
        }
        String account = form.getUserAccount().trim();
        if (account.length() < ACCOUNT_MIN_LENGTH || account.length() > ACCOUNT_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_LENGTH_ERROR);
        }
        form.setUserAccount(account);

        // 2. 强制必填：用户名称（非空+长度1-20位）→ USER_USERNAME_EMPTY(10005) / USER_USERNAME_TOO_LONG(10006)
        if (form.getUserUsername() == null || form.getUserUsername().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_USERNAME_EMPTY);
        }
        String username = form.getUserUsername().trim();
        if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_USERNAME_TOO_LONG);
        }
        form.setUserUsername(username);

        // 3. 选填：用户邮箱（默认"未填写邮箱+20位随机数"，填了则校验格式+长度≤25位）→ USER_EMAIL_TOO_LONG(10007) / USER_EMAIL_FORMAT_ERROR(10008)
        if (form.getUserEmail() == null || form.getUserEmail().trim().isEmpty()) {
            form.setUserEmail(DEFAULT_EMAIL_PREFIX + generateRandomNumber(20));
        } else {
            String email = form.getUserEmail().trim();
            if (email.length() > EMAIL_MAX_LENGTH) {
                throw new BusException(UserCodeEnum.USER_EMAIL_TOO_LONG);
            }
            if (!Pattern.matches(EMAIL_REGEX, email)) {
                throw new BusException(UserCodeEnum.USER_EMAIL_FORMAT_ERROR);
            }
            form.setUserEmail(email);
        }

        // 4. 选填：用户手机（默认"未填写手机号+5位随机数"，填了则校验格式）→ USER_PHONE_FORMAT_ERROR(10009)
        if (form.getUserPhone() == null || form.getUserPhone().trim().isEmpty()) {
            form.setUserPhone(DEFAULT_PHONE_PREFIX + generateRandomNumber(5));
        } else {
            String phone = form.getUserPhone().trim();
            if (!Pattern.matches(PHONE_REGEX, phone)) {
                throw new BusException(UserCodeEnum.USER_PHONE_FORMAT_ERROR);
            }
            form.setUserPhone(phone);
        }

        // 5. 强制必填：用户密码（非空+长度6-30位）→ USER_PASSWORD_EMPTY(10010) / USER_PASSWORD_LENGTH_ERROR(10011)
        if (form.getUserPassword() == null || form.getUserPassword().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_PASSWORD_EMPTY);
        }
        String password = form.getUserPassword().trim();
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_PASSWORD_LENGTH_ERROR);
        }
        form.setUserPassword(password);

        // 6. 选填：用户头像（默认图片URL，填了则校验长度≤255位）→ USER_IMAGE_TOO_LONG(10012)
        if (form.getUserImage() == null || form.getUserImage().trim().isEmpty()) {
            form.setUserImage(DEFAULT_IMAGE);
        } else {
            String image = form.getUserImage().trim();
            if (image.length() > IMAGE_MAX_LENGTH) {
                throw new BusException(UserCodeEnum.USER_IMAGE_TOO_LONG);
            }
            form.setUserImage(image);
        }

        // 7. 选填：用户简介（默认文案，填了则校验长度≤200位）→ USER_INTRODUCE_TOO_LONG(10013)
        if (form.getUserIntroduce() == null || form.getUserIntroduce().trim().isEmpty()) {
            form.setUserIntroduce(DEFAULT_INTRODUCE);
        } else {
            String introduce = form.getUserIntroduce().trim();
            if (introduce.length() > INTRODUCE_MAX_LENGTH) {
                throw new BusException(UserCodeEnum.USER_INTRODUCE_TOO_LONG);
            }
            form.setUserIntroduce(introduce);
        }

        // 8. 选填：用户目标岗位（不填则为null，无需校验）

        // 9. 选填：用户希望的最低月薪（默认0，校验≥0且≤100000）→ USER_MIN_SALARY_NEGATIVE(10016) / USER_MIN_SALARY_TOO_HIGH(10017)
        if (form.getUserDreamMinMonthSalary() == null) {
            form.setUserDreamMinMonthSalary(BigDecimal.ZERO);
        } else {
            BigDecimal minSalary = form.getUserDreamMinMonthSalary();
            if (minSalary.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusException(UserCodeEnum.USER_MIN_SALARY_NEGATIVE);
            }
            if (minSalary.compareTo(MAX_SALARY_LIMIT) > 0) {
                throw new BusException(UserCodeEnum.USER_MIN_SALARY_TOO_HIGH);
            }
        }

        // 10. 选填：用户希望的最高月薪（默认0，校验≥最低月薪）→ USER_MAX_SALARY_LESS_THAN_MIN(10018)
        if (form.getUserDreamMaxMonthSalary() == null) {
            form.setUserDreamMaxMonthSalary(BigDecimal.ZERO);
        } else {
            BigDecimal maxSalary = form.getUserDreamMaxMonthSalary();
            BigDecimal minSalary = form.getUserDreamMinMonthSalary();
            if (maxSalary.compareTo(minSalary) < 0) {
                throw new BusException(UserCodeEnum.USER_MAX_SALARY_LESS_THAN_MIN);
            }
        }

        // 11. 选填：用户希望的每周工作数（默认5，校验0-7之间）→ USER_WEEK_WORK_DAY_NUM_ILLEGAL(10019)
        if (form.getUserDreamWeekWorkDayNum() == 0) {
            form.setUserDreamWeekWorkDayNum(5);
        } else {
            int weekWorkDayNum = form.getUserDreamWeekWorkDayNum();
            if (weekWorkDayNum < MIN_WEEK_WORK_DAY || weekWorkDayNum > MAX_WEEK_WORK_DAY) {
                throw new BusException(UserCodeEnum.USER_WEEK_WORK_DAY_NUM_ILLEGAL);
            }
        }

        // 12. 选填：用户希望的福利待遇（默认文案，填了则校验长度≤200位）→ USER_GOOD_WELFARE_TOO_LONG(10014)
        if (form.getUserDreamGoodWelfare() == null || form.getUserDreamGoodWelfare().trim().isEmpty()) {
            form.setUserDreamGoodWelfare(DEFAULT_GOOD_WELFARE);
        } else {
            String welfare = form.getUserDreamGoodWelfare().trim();
            if (welfare.length() > GOOD_WELFARE_MAX_LENGTH) {
                throw new BusException(UserCodeEnum.USER_GOOD_WELFARE_TOO_LONG);
            }
            form.setUserDreamGoodWelfare(welfare);
        }

        // 13. 选填：用户地址-省级（默认"33"）
        if (form.getUserRecruitLocationFirst() == null || form.getUserRecruitLocationFirst().trim().isEmpty()) {
            form.setUserRecruitLocationFirst("33");
        }

        // 14. 选填：用户地址-市级（默认"99999"）
        if (form.getUserRecruitLocationSecond() == null || form.getUserRecruitLocationSecond().trim().isEmpty()) {
            form.setUserRecruitLocationSecond("99999");
        }

        // 15. 选填：用户大学编码（默认4001）
        if (form.getUserUniversityCode() == 0) {
            form.setUserUniversityCode(4001);
        }
    }

    /**
     * 私有辅助方法：生成指定位数的随机数字字符串
     */
    private static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
