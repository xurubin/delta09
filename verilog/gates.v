//gates

module dflip(output reg q, input d, input clk);
	always @ (posedge clk) begin
		q <= d;
	end
endmodule


module rslatch(output q1, output q2, input s, input r);
	wire a;
	wire b;
	nor(q1, r, q2);
	nor(q2, s, q1);
endmodule