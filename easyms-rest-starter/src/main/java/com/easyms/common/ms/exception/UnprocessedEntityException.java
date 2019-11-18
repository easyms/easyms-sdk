package com.easyms.common.ms.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sla.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnprocessedEntityException extends RuntimeException {

    private String message;
}
