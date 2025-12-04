package com.zyh.easyapplyresume.utils.uservalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * UserUpdateForm检查工具类，用于验证用户修改表单数据
 * 核心规则：
 * - 必填项：用户ID、用户账号、用户名称、用户密码、邮箱、手机号、头像、简介、最低月薪、最高月薪、每周工作数、福利待遇、省级地址、市级地址、大学编码
 * - 所有字段必填，为空则抛出对应异常，并进行相应的校验
 * @author shiningCloud2025
 */
public class UserUpdateValidator {

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

    /**
     * 用户修改校验规则：所有字段必填+相应格式校验
     */
    public static void validateForUpdate(UserUpdateForm form) {
        // 1. 必填：用户账号（非空+长度7-10位+首位不为0）→ USER_ACCOUNT_EMPTY(10003) / USER_ACCOUNT_LENGTH_ERROR(10004) / USER_ACCOUNT_FIRST_CHAR_ZERO(10005)
        if (form.getUserAccount() == null || form.getUserAccount().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_EMPTY);
        }
        String account = form.getUserAccount().trim();
        if (account.length() < ACCOUNT_MIN_LENGTH || account.length() > ACCOUNT_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_LENGTH_ERROR);
        }
        if (account.charAt(0) == '0') {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_FIRST_CHAR_ZERO);
        }
        form.setUserAccount(account);

        // 2. 必填：用户名称（非空+长度1-20位）→ USER_USERNAME_EMPTY(10006) / USER_USERNAME_TOO_LONG(10007)
        if (form.getUserUsername() == null || form.getUserUsername().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_USERNAME_EMPTY);
        }
        String username = form.getUserUsername().trim();
        if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_USERNAME_TOO_LONG);
        }
        form.setUserUsername(username);

        // 3. 必填：用户邮箱（非空+格式校验+长度≤25位）→ USER_EMAIL_TOO_LONG(10008) / USER_EMAIL_FORMAT_ERROR(10009)
        if (form.getUserEmail() == null || form.getUserEmail().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_EMAIL_FORMAT_ERROR);
        }
        String email = form.getUserEmail().trim();
        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_EMAIL_TOO_LONG);
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new BusException(UserCodeEnum.USER_EMAIL_FORMAT_ERROR);
        }
        form.setUserEmail(email);

        // 4. 必填：用户手机（非空+格式校验）→ USER_PHONE_FORMAT_ERROR(10010)
        if (form.getUserPhone() == null || form.getUserPhone().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_PHONE_FORMAT_ERROR);
        }
        String phone = form.getUserPhone().trim();
        if (!Pattern.matches(PHONE_REGEX, phone)) {
            throw new BusException(UserCodeEnum.USER_PHONE_FORMAT_ERROR);
        }
        form.setUserPhone(phone);

        // 5. 必填：用户密码（非空+长度6-30位）→ USER_PASSWORD_EMPTY(10011) / USER_PASSWORD_LENGTH_ERROR(10012)
        if (form.getUserPassword() == null || form.getUserPassword().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_PASSWORD_EMPTY);
        }
        String password = form.getUserPassword().trim();
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_PASSWORD_LENGTH_ERROR);
        }
        form.setUserPassword(password);

        // 6. 必填：用户头像（非空+长度≤255位）→ USER_IMAGE_EMPTY(10028) / USER_IMAGE_TOO_LONG(10013)
        if (form.getUserImage() == null || form.getUserImage().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_IMAGE_EMPTY);
        }
        String image = form.getUserImage().trim();
        if (image.length() > IMAGE_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_IMAGE_TOO_LONG);
        }
        form.setUserImage(image);

        // 7. 必填：用户简介（非空+长度≤200位）→ USER_INTRODUCE_EMPTY(10029) / USER_INTRODUCE_TOO_LONG(10014)
        if (form.getUserIntroduce() == null || form.getUserIntroduce().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_INTRODUCE_EMPTY);
        }
        String introduce = form.getUserIntroduce().trim();
        if (introduce.length() > INTRODUCE_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_INTRODUCE_TOO_LONG);
        }
        form.setUserIntroduce(introduce);

        // 8. 必填：用户目标岗位（不校验内容，只要有值即可）

        // 9. 必填：用户希望的最低月薪（非空+校验≥0且≤100000）→ USER_DREAM_MIN_SALARY_EMPTY(10030) / USER_MIN_SALARY_NEGATIVE(10017) / USER_MIN_SALARY_TOO_HIGH(10018)
        if (form.getUserDreamMinMonthSalary() == null) {
            throw new BusException(UserCodeEnum.USER_DREAM_MIN_SALARY_EMPTY);
        }
        BigDecimal minSalary = form.getUserDreamMinMonthSalary();
        if (minSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusException(UserCodeEnum.USER_MIN_SALARY_NEGATIVE);
        }
        if (minSalary.compareTo(MAX_SALARY_LIMIT) > 0) {
            throw new BusException(UserCodeEnum.USER_MIN_SALARY_TOO_HIGH);
        }

        // 10. 必填：用户希望的最高月薪（非空+校验≥最低月薪）→ USER_DREAM_MAX_SALARY_EMPTY(10031) / USER_MAX_SALARY_LESS_THAN_MIN(10019)
        if (form.getUserDreamMaxMonthSalary() == null) {
            throw new BusException(UserCodeEnum.USER_DREAM_MAX_SALARY_EMPTY);
        }
        BigDecimal maxSalary = form.getUserDreamMaxMonthSalary();
        if (maxSalary.compareTo(minSalary) < 0) {
            throw new BusException(UserCodeEnum.USER_MAX_SALARY_LESS_THAN_MIN);
        }

        // 11. 必填：用户希望的每周工作数（非0+校验0-7之间）→ USER_DREAM_WEEK_WORK_DAY_EMPTY(10032) / USER_WEEK_WORK_DAY_NUM_ILLEGAL(10020)
        if (form.getUserDreamWeekWorkDayNum() == 0) {
            throw new BusException(UserCodeEnum.USER_DREAM_WEEK_WORK_DAY_EMPTY);
        }
        int weekWorkDayNum = form.getUserDreamWeekWorkDayNum();
        if (weekWorkDayNum < MIN_WEEK_WORK_DAY || weekWorkDayNum > MAX_WEEK_WORK_DAY) {
            throw new BusException(UserCodeEnum.USER_WEEK_WORK_DAY_NUM_ILLEGAL);
        }

        // 12. 必填：用户希望的福利待遇（非空+长度≤200位）→ USER_DREAM_GOOD_WELFARE_EMPTY(10033) / USER_GOOD_WELFARE_TOO_LONG(10015)
        if (form.getUserDreamGoodWelfare() == null || form.getUserDreamGoodWelfare().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_DREAM_GOOD_WELFARE_EMPTY);
        }
        String welfare = form.getUserDreamGoodWelfare().trim();
        if (welfare.length() > GOOD_WELFARE_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_GOOD_WELFARE_TOO_LONG);
        }
        form.setUserDreamGoodWelfare(welfare);

        // 13. 必填：用户地址（省份和城市都必须不为空）→ USER_RECRUIT_LOCATION_EMPTY(10034) / USER_LOCATION_INCOMPLETE(10027)
        String locationFirst = form.getUserRecruitLocationFirst();
        String locationSecond = form.getUserRecruitLocationSecond();
        boolean firstEmpty = locationFirst == null || locationFirst.trim().isEmpty();
        boolean secondEmpty = locationSecond == null || locationSecond.trim().isEmpty();
        
        if (firstEmpty || secondEmpty) {
            throw new BusException(UserCodeEnum.USER_RECRUIT_LOCATION_EMPTY);
        }

        // 14. 必填：用户大学编码（非0）→ USER_UNIVERSITY_CODE_EMPTY(10035)
        if (form.getUserUniversityCode() == 0) {
            throw new BusException(UserCodeEnum.USER_UNIVERSITY_CODE_EMPTY);
        }
    }
}
