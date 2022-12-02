package com.example.demo.response.paper;

import com.example.demo.response.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class SavePaperResponse  extends ResponseBody{
	private String message;
}
