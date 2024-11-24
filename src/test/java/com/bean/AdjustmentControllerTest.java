package com.bean;

import com.bean.domain.Adjustment;
import com.bean.repository.AdjustmentRepository;
import com.bean.controller.AdjustmentController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class AdjustmentControllerTest {

    @Mock
    private AdjustmentRepository adjustmentRepository;

    @InjectMocks
    private AdjustmentController adjustmentController;

    private MockMvc mockMvc;

    @Test
    public void testFindAdjustmentsByEmployeeId() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adjustmentController).build();

        Adjustment adjustment1 = new Adjustment();
        adjustment1.setToId(1L);
        adjustment1.setAmount(100);

        Adjustment adjustment2 = new Adjustment();
        adjustment2.setToId(1L);
        adjustment2.setAmount(200);

        List<Object[]> adjustments = Arrays.asList(
            new Object[]{adjustment1},
            new Object[]{adjustment2}
        );

        when(adjustmentRepository.findAdjustmentsByEmployeeId(1L)).thenReturn(adjustments);

        mockMvc.perform(get("/api/v1/adjustment/findAdjustmentsByEmployeeId")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", is(-100)))
                .andExpect(jsonPath("$[1].amount", is(-200)));
    }
}