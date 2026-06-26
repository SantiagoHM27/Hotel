package com.auth.auth.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje

) {}
