package it.vodafone.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.vodafone.test.dto.AbstractTaxCode;
import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.dto.TaxCode;
import it.vodafone.test.service.TaxCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/taxcode")
@RequiredArgsConstructor
public class TaxCodeController {

    private final TaxCodeService taxCodeService;

    @Operation(description = "Extract all the components of a tax code", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Taxcode is syntactically wrong")
    })
    @PostMapping(value = "/component", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public AbstractTaxCode taxCodeComponents(@Valid @RequestBody TaxCode taxCode
            , HttpServletRequest httpServletRequest) {
        return taxCodeService.generateTaxCodeComponents(taxCode);
    }

    @Operation(description = "Generate a valid taxcode from its components", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "At least a components is syntactically wrong")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxCode taxCodeFromComponents( @Valid @RequestBody PersonTaxCode nodeDto) {
        return taxCodeService.taxCodeFromComponents(nodeDto);
    }

}
