--
--
--  This file is a part of JOP, the Java Optimized Processor
--
--  Copyright (C) 2001-2008, Martin Schoeberl (martin@jopdesign.com)
--
--  This program is free software: you can redistribute it and/or modify
--  it under the terms of the GNU General Public License as published by
--  the Free Software Foundation, either version 3 of the License, or
--  (at your option) any later version.
--
--  This program is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU General Public License for more details.
--
--  You should have received a copy of the GNU General Public License
--  along with this program.  If not, see <http://www.gnu.org/licenses/>.
--


--
--	jop_DE2.vhd
--
--	top level for Altera DE2 board
--	With 8 MBytes SDRAM and Memory-mapped I/O for LED,switch etc.
--
--	2009-01-19	adapted from jop256x16.vhd
--
--


library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

use work.jop_types.all;
use work.sc_pack.all;
use work.jop_config.all;


entity jop is

generic (
	ram_cnt		: integer := 2;		-- clock cycles for external ram
--	rom_cnt		: integer := 3;		-- clock cycles for external rom OK for 20 MHz
	rom_cnt		: integer := 15;	-- clock cycles for external rom for 100 MHz
	jpc_width	: integer := 12;	-- address bits of java bytecode pc = cache size
	block_bits	: integer := 4;		-- 2*block_bits is number of cache blocks
	spm_width	: integer := 0		-- size of scratchpad RAM (in number of address bits for 32-bit words)
);

port (
	clk				: in std_logic;
--
--	serial interface
--
	ser_txd			: out std_logic;
	ser_rxd			: in std_logic;

--
--	watchdog
--
	wd				: out std_logic;

--
--	only one ram bank
--
	rama_a		: out std_logic_vector(17 downto 0);
	rama_d		: inout std_logic_vector(15 downto 0);
	rama_ncs	: out std_logic;
	rama_noe	: out std_logic;
	rama_nlb	: out std_logic;
	rama_nub	: out std_logic;
	rama_nwe	: out std_logic;

--
--	DE2 Switches and LEDs
--
	SW			: in std_logic_vector(17  downto 0);
	KEY			: in std_logic_vector(3   downto 0);
	LEDR		: out std_logic_vector(17 downto 0);
	LEDG		: out std_logic_vector(7  downto 0);
	HEX0		: out std_logic_vector(6  downto 0);
	HEX1		: out std_logic_vector(6  downto 0);
	HEX2		: out std_logic_vector(6  downto 0);
	HEX3		: out std_logic_vector(6  downto 0);
	HEX4		: out std_logic_vector(6  downto 0);
	HEX5		: out std_logic_vector(6  downto 0);
	HEX6		: out std_logic_vector(6  downto 0);
	HEX7		: out std_logic_vector(6  downto 0);
	TDI		: in std_logic;
	TCS		: in std_logic;
	TCK		: in std_logic;
	TDO		: out std_logic;
--
--	DE2 SDRAM
--
	DRAM_ADDR	: out std_logic_vector(11  downto 0);
	DRAM_DQ		: inout std_logic_vector(15 downto 0);
	DRAM_BA_0	: out std_logic;
	DRAM_BA_1	: out std_logic;
	DRAM_CAS_N	: out std_logic;
	DRAM_CKE	: out std_logic;
	DRAM_CLK	: out std_logic;
	DRAM_CS_N	: out std_logic;
	DRAM_LDQM	: out std_logic;
	DRAM_UDQM	: out std_logic;
	DRAM_RAS_N	: out std_logic;
	DRAM_WE_N	: out std_logic
);
end jop;

architecture rtl of jop is

--
--	components:
--

component cyc2_pll is
generic (multiply_by : natural; divide_by : natural);
port (
	inclk0		: in std_logic;
	c0			: out std_logic;
	c1			: out std_logic;
	locked		: out std_logic
	
);
end component;


--
--	Signals
--
	signal clk_int			: std_logic;

	signal int_res			: std_logic;
	signal res_cnt			: unsigned(2 downto 0) := "000";	-- for the simulation

	attribute altera_attribute : string;
	attribute altera_attribute of res_cnt : signal is "POWER_UP_LEVEL=LOW";

--
--	jopcpu connections
--
	signal sc_mem_out		: sc_out_type;
	signal sc_mem_in		: sc_in_type;
	signal sc_io_out		: sc_out_type;
	signal sc_io_in			: sc_in_type;
	signal irq_in			: irq_bcf_type;
	signal irq_out			: irq_ack_type;
	signal exc_req			: exception_type;

--
--	IO interface
--
	signal ser_in			: ser_in_type;
	signal ser_out			: ser_out_type;
	signal wd_out			: std_logic;

	-- for generation of internal reset
-- not available at this board:
	signal ser_ncts			: std_logic;
	signal ser_nrts			: std_logic;
	
-- remove the comment for RAM access counting
-- signal ram_count		: std_logic;
signal mTCK				:std_logic;
signal sdram_clk_i		:std_logic;
signal sdram_clk_locked :std_logic;
		
signal sdram_count : std_logic_vector(31 downto 0);
signal external_reset	:std_logic;
begin
cmp_clk_clock: work.clk_clock
	port map(
		inclk => TCK,
		outclk => mTCK
		);

	ser_ncts <= '0';
--
--	reset
--	extern reset by holding Key[0..3]
--

external_reset <= (not KEY(0)) and (not KEY(1)) and (not KEY(2)) and (not KEY(3));
process(clk_int)
begin
	if rising_edge(clk_int) then
		if (res_cnt/="111") then
			res_cnt <= res_cnt+1;
		end if;

		int_res <= not res_cnt(0) or not res_cnt(1) or not res_cnt(2) or external_reset;
	end if;
end process;

--
--	components of jop
--
	pll_inst : cyc2_pll generic map(
		multiply_by => pll_mult,
		divide_by => pll_div
	)
	port map (
		inclk0	 => clk,
		c0	 => clk_int,
		c1   => sdram_clk_i,
		locked => sdram_clk_locked
	);
-- clk_int <= clk;

	wd <= wd_out;


	cpm_cpu: entity work.jopcpu
		generic map(
			jpc_width => jpc_width,
			block_bits => block_bits,
			spm_width => spm_width
		)
		port map(clk_int, int_res,
			sc_mem_out, sc_mem_in,
			sc_io_out, sc_io_in,
			irq_in, irq_out, exc_req);

	cmp_io: entity work.scio 
		port map (clk_int, int_res,
			sc_io_out, sc_io_in,
			irq_in, irq_out, exc_req,

			txd => ser_txd,
			rxd => ser_rxd,
			ncts => ser_ncts,
			nrts => ser_nrts,
			wd => wd_out,
			
			SW	=> SW,		
			KEY	=> KEY,
			LEDR => LEDR,
			LEDG => LEDG,
			HEX0 => HEX0,
			HEX1 => HEX1,
			HEX2 => HEX2,
			HEX3 => HEX3,
			HEX4 => HEX4,
			HEX5 => HEX5,
			HEX6 => HEX6,
			HEX7 => HEX7,
			TDI => TDI,
			TCS => TCS,
			TCK => mTCK,
			TDO => TDO,
			debug => open,
			
			l => open,
			r => open,
			t => open,
			b => open
			-- remove the comment for RAM access counting
			-- ram_cnt => ram_count
		);

	cmp_scm: entity work.sc_de2_mem_if
		generic map (
			ram_cnt => ram_cnt
		)
		port map (clk_int, int_res,
			sc_mem_out, sc_mem_in,


	--- SRAM Connnections
			ext_sram_addr => rama_a,
			ext_sram_d => rama_d,
			ext_sram_ncs => rama_ncs,
			ext_sram_noe => rama_noe,
			ext_sram_nwe => rama_nwe,
			ext_sram_nlb => rama_nlb,
			ext_sram_nub => rama_nub,
	
	--- SDRAM Connections
			ext_dram_clk_i => sdram_clk_i,
			ext_dram_clk_locked => sdram_clk_locked,
			ext_dram_addr => DRAM_ADDR,
			ext_dram_dq => DRAM_DQ,
			ext_dram_ba_0 => DRAM_BA_0,
			ext_dram_ba_1 => DRAM_BA_1,
			ext_dram_ldqm => DRAM_LDQM,
			ext_dram_udqm => DRAM_UDQM,
			ext_dram_ras_n => DRAM_RAS_N,
			ext_dram_cas_n => DRAM_CAS_N,
			ext_dram_cke => DRAM_CKE,
			ext_dram_clk => DRAM_CLK,
			ext_dram_we_n => DRAM_WE_N,
			ext_dram_cs_n => DRAM_CS_N,
			
			sdram_count => sdram_count
		);
--LEDR <= sdram_count(25 downto 8);
--LEDG(7 downto 0) <= sdram_count(7 downto 0);
end rtl;
