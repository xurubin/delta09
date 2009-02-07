module rom16(output reg result, input [3:0] address, input clk);
	reg [15:0] data;
	
	initial begin
		data <= 16'b1010101010101010;
	end
	always @(posedge clk) begin
		result <= data[address];
	end
endmodule

module ram16(output reg result, input write_enable, input set, input [3:0] address, input clk);
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
