--
-- xram_block.vhd
--
-- Generated by BlockGen
-- 2009-1-27 20:44:21
--
-- This module will synthesize on Spartan2/2E and Virtex/E devices.
--

library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;
library unisim;
use unisim.vcomponents.all;

entity xram_block is
	port (
		a_rst  : in std_logic;
		a_clk  : in std_logic;
		a_en   : in std_logic;
		a_wr   : in std_logic;
		a_addr : in std_logic_vector(9 downto 0);
		a_din  : in std_logic_vector(31 downto 0);
		a_dout : out std_logic_vector(31 downto 0);
		b_rst  : in std_logic;
		b_clk  : in std_logic;
		b_en   : in std_logic;
		b_wr   : in std_logic;
		b_addr : in std_logic_vector(9 downto 0);
		b_din  : in std_logic_vector(31 downto 0);
		b_dout : out std_logic_vector(31 downto 0)
	);
end xram_block;

architecture rtl of xram_block is

	component RAMB4_S4_S4
		port (
			DIA    : in std_logic_vector (3 downto 0);
			DIB    : in std_logic_vector (3 downto 0);
			ENA    : in std_logic;
			ENB    : in std_logic;
			WEA    : in std_logic;
			WEB    : in std_logic;
			RSTA   : in std_logic;
			RSTB   : in std_logic;
			CLKA   : in std_logic;
			CLKB   : in std_logic;
			ADDRA  : in std_logic_vector (9 downto 0);
			ADDRB  : in std_logic_vector (9 downto 0);
			DOA    : out std_logic_vector (3 downto 0);
			DOB    : out std_logic_vector (3 downto 0)
		); 
	end component;

	attribute INIT: string;
	attribute INIT_00: string;
	attribute INIT_01: string;
	attribute INIT_02: string;
	attribute INIT_03: string;
	attribute INIT_04: string;
	attribute INIT_05: string;
	attribute INIT_06: string;
	attribute INIT_07: string;
	attribute INIT_08: string;
	attribute INIT_09: string;
	attribute INIT_0a: string;
	attribute INIT_0b: string;
	attribute INIT_0c: string;
	attribute INIT_0d: string;
	attribute INIT_0e: string;
	attribute INIT_0f: string;

	attribute INIT_00 of cmp_ram_0: label is "0a88888860fe50ff53ff88412014076088888888800000000000000000000000";
	attribute INIT_01 of cmp_ram_0: label is "8888888888888888888888888888888888888888888888888888888888888888";
	attribute INIT_02 of cmp_ram_0: label is "8888888888888888888888888888888888888888888888888888888888888888";
	attribute INIT_03 of cmp_ram_0: label is "8888888888888888888888888888888888888888888888888888888888888888";
	attribute INIT_04 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_0: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_1: label is "03777777001f88ff00f7b009090b088477777777700000000000000000000000";
	attribute INIT_01 of cmp_ram_1: label is "7777777777777777777777777777777777777777777777777777777777777777";
	attribute INIT_02 of cmp_ram_1: label is "7777777777777777777777777777777777777777777777777777777777777777";
	attribute INIT_03 of cmp_ram_1: label is "7777777777777777777777777777777777777777777777777777777777777777";
	attribute INIT_04 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_1: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_2: label is "0a666666000fff0f00f0f00f0f0f0ff066666666600000000000000000000000";
	attribute INIT_01 of cmp_ram_2: label is "6666666666666666666666666666666666666666666666666666666666666666";
	attribute INIT_02 of cmp_ram_2: label is "6666666666666666666666666666666666666666666666666666666666666666";
	attribute INIT_03 of cmp_ram_2: label is "6666666666666666666666666666666666666666666666666666666666666666";
	attribute INIT_04 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_2: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_3: label is "06555555000fff0f00f0f00f0f0f0ff055555555500000000000000000000000";
	attribute INIT_01 of cmp_ram_3: label is "5555555555555555555555555555555555555555555555555555555555555555";
	attribute INIT_02 of cmp_ram_3: label is "5555555555555555555555555555555555555555555555555555555555555555";
	attribute INIT_03 of cmp_ram_3: label is "5555555555555555555555555555555555555555555555555555555555555555";
	attribute INIT_04 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_3: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_4: label is "02444444000fff0000f0f00f0f0f0ff044444444400000000000000000000000";
	attribute INIT_01 of cmp_ram_4: label is "4444444444444444444444444444444444444444444444444444444444444444";
	attribute INIT_02 of cmp_ram_4: label is "4444444444444444444444444444444444444444444444444444444444444444";
	attribute INIT_03 of cmp_ram_4: label is "4444444444444444444444444444444444444444444444444444444444444444";
	attribute INIT_04 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_4: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_5: label is "03333333000fff0000f0f00f0f0f0ff033333333300000000000000000000000";
	attribute INIT_01 of cmp_ram_5: label is "3333333333333333333333333333333333333333333333333333333333333333";
	attribute INIT_02 of cmp_ram_5: label is "3333333333333333333333333333333333333333333333333333333333333333";
	attribute INIT_03 of cmp_ram_5: label is "3333333333333333333333333333333333333333333333333333333333333333";
	attribute INIT_04 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_5: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_6: label is "01222222000fff0000f0f00f0f0f0ff022222222200000000000000000000000";
	attribute INIT_01 of cmp_ram_6: label is "2222222222222222222222222222222222222222222222222222222222222222";
	attribute INIT_02 of cmp_ram_6: label is "2222222222222222222222222222222222222222222222222222222222222222";
	attribute INIT_03 of cmp_ram_6: label is "2222222222222222222222222222222222222222222222222222222222222222";
	attribute INIT_04 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_6: label is "0000000000000000000000000000000000000000000000000000000000000000";

	attribute INIT_00 of cmp_ram_7: label is "00111111080fff0000f0f00f0f0f0ff011111111100000000000000000000000";
	attribute INIT_01 of cmp_ram_7: label is "1111111111111111111111111111111111111111111111111111111111111111";
	attribute INIT_02 of cmp_ram_7: label is "1111111111111111111111111111111111111111111111111111111111111111";
	attribute INIT_03 of cmp_ram_7: label is "1111111111111111111111111111111111111111111111111111111111111111";
	attribute INIT_04 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_05 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_06 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_07 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_08 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_09 of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0a of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0b of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0c of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0d of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0e of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";
	attribute INIT_0f of cmp_ram_7: label is "0000000000000000000000000000000000000000000000000000000000000000";

begin

	cmp_ram_0 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(3 downto 0),
			ADDRA => a_addr,
			DOA => a_dout(3 downto 0),
			DIB => b_din(3 downto 0),
			ADDRB => b_addr,
			DOB => b_dout(3 downto 0)
		);

	cmp_ram_1 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(7 downto 4),
			ADDRA => a_addr,
			DOA => a_dout(7 downto 4),
			DIB => b_din(7 downto 4),
			ADDRB => b_addr,
			DOB => b_dout(7 downto 4)
		);

	cmp_ram_2 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(11 downto 8),
			ADDRA => a_addr,
			DOA => a_dout(11 downto 8),
			DIB => b_din(11 downto 8),
			ADDRB => b_addr,
			DOB => b_dout(11 downto 8)
		);

	cmp_ram_3 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(15 downto 12),
			ADDRA => a_addr,
			DOA => a_dout(15 downto 12),
			DIB => b_din(15 downto 12),
			ADDRB => b_addr,
			DOB => b_dout(15 downto 12)
		);

	cmp_ram_4 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(19 downto 16),
			ADDRA => a_addr,
			DOA => a_dout(19 downto 16),
			DIB => b_din(19 downto 16),
			ADDRB => b_addr,
			DOB => b_dout(19 downto 16)
		);

	cmp_ram_5 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(23 downto 20),
			ADDRA => a_addr,
			DOA => a_dout(23 downto 20),
			DIB => b_din(23 downto 20),
			ADDRB => b_addr,
			DOB => b_dout(23 downto 20)
		);

	cmp_ram_6 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(27 downto 24),
			ADDRA => a_addr,
			DOA => a_dout(27 downto 24),
			DIB => b_din(27 downto 24),
			ADDRB => b_addr,
			DOB => b_dout(27 downto 24)
		);

	cmp_ram_7 : RAMB4_S4_S4
		port map (
			WEA => a_wr,
			WEB => b_wr,
			ENA => a_en,
			ENB => b_en,
			RSTA => a_rst,
			RSTB => b_rst,
			CLKA => a_clk,
			CLKB => b_clk,
			DIA => a_din(31 downto 28),
			ADDRA => a_addr,
			DOA => a_dout(31 downto 28),
			DIB => b_din(31 downto 28),
			ADDRB => b_addr,
			DOB => b_dout(31 downto 28)
		);

end rtl;
