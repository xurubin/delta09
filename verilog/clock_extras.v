 module clock_div(input clk, input[31:0] div, input[31:0] mul, output reg enable);
	reg [31:0] i;
	always@(posedge clk) begin
		if(i < div) begin
			i <= i + mul;
		end
		else begin
			enable <= ~enable;
			i <= 0;
		end
	end
endmodule

module get_clock(input clk, input [31:0] div, input [31:0] mul, output div_clk);
	wire enable;
	clock_div d(clk, div, mul, enable);
	custom_clock(clk, enable, div_clk);
endmodule