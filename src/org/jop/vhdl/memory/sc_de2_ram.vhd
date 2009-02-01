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
--	sc_de2_ram.vhd
--
--	SimpCon compliant external memory interface
--	for 16-bit SRAM (e.g. Altera DE2 board) High 16-bit word is at lower address
--	PLUS 16-bit SDRAM 8MBytes
--  TODO: PLUS Flash Memory
--	Connection between mem_sc and the external memory bus
--
--	memory mapping
--	
--		x000000-x07ffff	external SRAM (w mirror)	max. 512 kW (4*4 MBit)
--		x080000-x7fffff	external SDRAM (w mirror)	max. 7.5 MB
--		x800000-xbfffff external Flash				max. 4 MB
--	RAM: 16 bit word
--
--


Library IEEE;
use IEEE.std_logic_1164.all;
use ieee.numeric_std.all;

use work.jop_types.all;
use work.sc_pack.all;

entity sc_de2_mem_if is
generic (ram_cnt:integer);

port (

	clk, reset	: in std_logic;

--
--	SimpCon memory interface
--
	sc_mem_out		: in sc_out_type;
	sc_mem_in		: out sc_in_type;

-- memory interface

	--- SRAM Connnections
	ext_sram_addr		: out std_logic_vector(18-1 downto 0);
	ext_sram_d			: inout std_logic_vector(15 downto 0);
	ext_sram_ncs		: out std_logic;
	ext_sram_noe		: out std_logic;
	ext_sram_nwe		: out std_logic;
	ext_sram_nlb	: out std_logic;
	ext_sram_nub	: out std_logic;
	
	--- SDRAM Connections
	ext_dram_clk_i		: in std_logic;
	ext_dram_clk_locked	: in std_logic;
	ext_dram_addr		: out std_logic_vector(11 downto 0);
	ext_dram_dq			: inout std_logic_vector(15 downto 0);
	ext_dram_ba_0		: out std_logic;
	ext_dram_ba_1		: out std_logic;
	ext_dram_ldqm		: out std_logic;
	ext_dram_udqm		: out std_logic;
	ext_dram_ras_n		: out std_logic;
	ext_dram_cas_n		: out std_logic;
	ext_dram_cke		: out std_logic;
	ext_dram_clk		: out std_logic;
	ext_dram_we_n		: out std_logic;
	ext_dram_cs_n		: out std_logic;
	
	----counter
	sdram_count			: out std_logic_vector(31 downto 0)
	
);
end sc_de2_mem_if;

architecture rtl of sc_de2_mem_if is



-- SRAM memory interface
	signal sram_sc_mem_out		: sc_out_type;
	signal sram_sc_mem_in		: sc_in_type;
	signal sram_addr		: std_logic_vector(18-1 downto 0);
	signal sram_dout		: std_logic_vector(15 downto 0);
	signal sram_din			: std_logic_vector(15 downto 0);
	signal sram_dout_en		: std_logic;
	signal sram_ncs			: std_logic;
	signal sram_noe			: std_logic;
	signal sram_nwe			: std_logic;

	signal sdram_sc_mem_out		: sc_out_type;
	signal sdram_sc_mem_in		: sc_in_type;

	signal flash_sc_mem_out		: sc_out_type;
	signal flash_sc_mem_in		: sc_in_type;
	
	signal sram_select :std_logic;
	signal sdram_select :std_logic;
	signal ram_sel :std_logic;
	signal mem_access: std_logic;
	
	signal sdram_counter		: std_logic_vector(31 downto 0);
	
begin

process (sc_mem_out) begin
	sram_select <= '0';
	sdram_select <= '0';
	case sc_mem_out.address(20 downto 17) is 
		when "0000" =>	
			sram_select <= '1';
		when others	=>	
			sdram_select <= '1';
	end case;
end process;

mem_access <= sram_sc_mem_out.rd or sram_sc_mem_out.wr or sdram_sc_mem_out.rd or sdram_sc_mem_out.wr;

sdram_count <= sdram_counter;
process (clk,reset) begin
  if (reset = '1') then
	ram_sel <= '0';
	sdram_counter <= (others => '0');
  elsif rising_edge(clk) then
	case sc_mem_out.address(20 downto 17) is 
		when "0000" =>	
			if (mem_access='1') then
				ram_sel <= '0';
			end if;
		when others	=>	
			if (mem_access='1') then
				ram_sel <= '1';
				sdram_counter <= std_logic_vector(unsigned(sdram_counter) + 1);
			end if;
	end case;
  end if;
end process;
sc_mem_in <= sram_sc_mem_in when ram_sel = '0' else sdram_sc_mem_in;

sram_sc_mem_out.address <= sc_mem_out.address;-- when sram_select='1' else (others=>'x');
sram_sc_mem_out.wr_data <= sc_mem_out.wr_data;-- when sram_select='1' else (others=>'x');
sram_sc_mem_out.rd 		<= sc_mem_out.rd when sram_select='1' else '0';
sram_sc_mem_out.wr 		<= sc_mem_out.wr when sram_select='1' else '0';

sdram_sc_mem_out.address 	<= sc_mem_out.address;-- when sram_select='0' else (others=>'x');
sdram_sc_mem_out.wr_data 	<= sc_mem_out.wr_data;-- when sram_select='0' else (others=>'x');
sdram_sc_mem_out.rd 		<= sc_mem_out.rd when sram_select='0' else '0';
sdram_sc_mem_out.wr 		<= sc_mem_out.wr when sram_select='0' else '0';
	
-------------------------------------------------------------------------
--------------------Implement SRAM Interface-----------------------------
	cmp_scm: entity work.sc_mem_if
		generic map (
			ram_ws => ram_cnt-1
		)
		port map (clk => clk, reset => reset,
			sc_mem_out => sram_sc_mem_out, sc_mem_in => sram_sc_mem_in,

			ram_addr => sram_addr,
			ram_dout => sram_dout,
			ram_din => sram_din,
			ram_dout_en	=> sram_dout_en,
			ram_ncs => sram_ncs,
			ram_noe => sram_noe,
			ram_nwe => sram_nwe
		);

	process(sram_dout_en, sram_dout)
	begin
		if sram_dout_en='1' then
			ext_sram_d <= sram_dout;
		else
			ext_sram_d <= (others => 'Z');
		end if;
	end process;

	sram_din <= ext_sram_d;
	
	-- remove the comment for RAM access counting
	-- ram_count <= ram_ncs;

--
--	To put this RAM address in an output register
--	we have to make an assignment (FAST_OUTPUT_REGISTER)
--
	ext_sram_addr <= sram_addr(17 downto 0);
	ext_sram_ncs <= sram_ncs;
	ext_sram_noe <= sram_noe;
	ext_sram_nwe <= sram_nwe;
	ext_sram_nlb <= '0';
	ext_sram_nub <= '0';
-------------------------------------------------------------------------
--------------------Implement SRAM Interface Finished--------------------


-------------------------------------------------------------------------
--------------------Implement SDRAM Interface----------------------------
cmp_sdram: work.sc_sdram_if
port map(
	clk => clk,
	reset => reset,
--	SimpCon memory interface
	sc_mem_out => sdram_sc_mem_out,
	sc_mem_in => sdram_sc_mem_in, 
-- memory interface
	ext_dram_clk_i => ext_dram_clk_i,
	ext_dram_clk_locked	=> ext_dram_clk_locked,
	ext_dram_addr => ext_dram_addr,
	ext_dram_dq	=> ext_dram_dq,
	ext_dram_ba_0 => ext_dram_ba_0,
	ext_dram_ba_1 => ext_dram_ba_1,
	ext_dram_ldqm => ext_dram_ldqm,
	ext_dram_udqm => ext_dram_udqm ,
	ext_dram_ras_n => ext_dram_ras_n,
	ext_dram_cas_n => ext_dram_cas_n,
	ext_dram_cke => ext_dram_cke,
	ext_dram_clk => ext_dram_clk,
	ext_dram_we_n => ext_dram_we_n,
	ext_dram_cs_n => ext_dram_cs_n
);
-------------------------------------------------------------------------
--------------------Implement SDRAM Interface finish---------------------

end rtl;
