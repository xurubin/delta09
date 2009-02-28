module rom16(output reg result, input a0, input a1, input a2, input a3, input [15:0] data, input clk);
	wire [3:0] address;
	assign address = {a0, a1, a2, a3};
	always @(posedge clk) begin
		result <= data[address];
	end
endmodule


module ram16(output reg result, input write_enable, input set, input a0, input a1, input a2, input a3, input clk);
	wire [3:0] address;
	assign address = {a0, a1, a2, a3};
	reg [15:0] data;
	always @(posedge clk) begin
		if(write_enable) begin
			data[address] <= set;
			result <= set;
		end
		else begin
			result <= data[address];
		end
	end
endmodule 
