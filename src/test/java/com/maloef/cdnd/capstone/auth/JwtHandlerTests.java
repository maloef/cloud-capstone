package com.maloef.cdnd.capstone.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtHandlerTests {

    JwtHandler jwtVerifier = new JwtHandler();
    String expiredJwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkNjVllhSzZpa3pzbXJoTUl4REJQdCJ9.eyJpc3MiOiJodHRwczovL2Rldi1iNWw4cHhnN3ozc251amR5LnVzLmF1dGgwLmNvbS8iLCJhdWQiOiJDWjUyVUFuVWdBVU53RnJSb3E2bHNFMjByM2JXalZBRyIsImlhdCI6MTY3ODI5NTIxMCwiZXhwIjoxNjc4MzMxMjEwLCJzdWIiOiJhdXRoMHw2M2ZkZTRhZjc4ZjNkYTBjZTQ4MGE2NWEiLCJhdF9oYXNoIjoiVkE3RTJDWTdqWnMzQzB6dDlYQlc0ZyIsInNpZCI6IlJrX3BEeXdTTE5SSG14bHZqbXM2ek9yR0VNeU5vTmtGIiwibm9uY2UiOiJIY0hySkxIR1VvNnQucnh2bkdyZ0V3N0ZaNEJlZFMwbSJ9.XAVAfINUo0bMZXoHLB9hA3jfOAChOzTUC_af_1kPmyYounfisHXfs36N1xcR75v6pNQUDfGOjKj5rCXlItkrktIpFw97qKK8U12KEOuZNMDsgJZvNHNWC37RSS9VajBQzEZP-TXu72tH9um6RKpin7KTcS26mtuNTG_jZxR9IF4bujrsKGSHdWRkJJi-RHZFLOf7gYagWfmkIyhnXPUOkLFei7qo-o8i9InMtIi_fdCcmm7LQ-GpwDiT1zIy6-5zcWpTewKipF2KbqGEf_IYHzfJkretHM2BEn1WfL7D3uBF28OePN969uhZysF4Ge0alSc8KrmsQSjQVKm81BZPrQ";
    String malformedJwt = "soguhwergh";

    @Test
    void verify_expiredJwt_throwsExpiredJwtException() {
        assertThrows(ExpiredJwtException.class, () -> jwtVerifier.extractUserIdFromToken(expiredJwt));
    }

    @Test
    void verify_malformedJwt_throwsMalformedJwtException() {
        assertThrows(MalformedJwtException.class, () -> jwtVerifier.extractUserIdFromToken(malformedJwt));
    }
}
