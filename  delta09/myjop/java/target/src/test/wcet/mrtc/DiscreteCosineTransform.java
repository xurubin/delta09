package wcet.mrtc;
//import edu.uci.eecs.doc.clepsydra.loop.LoopBound;

/**
 * Forward Discrete Cosine Transform. Used on 8x8 image blocks to reassemble blocks
 * in order to ease quantization compressing image information on the more
 * significant frequency components.
 *
 * Expected Result:
 * block = { 699,164,-51,-16, 31,-15,-19,  8,
 *            71, 14,-61, -2,-11,-12,  7, 12,
 *           -58,-55, 13, 28,-20, -7, 14,-18,
 *            29, 22,  3,  3,-11,  7, 11,-22,
 *            -1,-28,-27, 10,  0, -7, 11,  6,
 *             7,  6, 21, 21,-10, -8,  2,-14,
 *             1, -7,-15,-15,-10, 15, 16,-10,
 *             0, -1,  0, 15,  4,-13, -5,  4 };
 *
 * Hexadecimal result:
 * block = 02bb00a4 ffcdfff0 001ffff1 ffed0008 0047000e ffc3fffe 000bfff4 0007000c
 *         ffc6ffc9 000d001c ffecfff9 000effee 001d0016 00030003 fff50007 000bffea
 *         ffffffe4 ffe5000a 0000fff9 000b0006 00070006 00150015 fff6fff8 0002fff2
 *         0001fff9 fff1fff1 fff6000f 0010fff6 0000ffff 0000000f 0004fff3 fffb0004
 *
 * WCET aspect: A lot of calculations based on integer array elements.
 *
 * Ported from C code written by Jan Gustafsson for the <a
 * href="http://www.mrtc.mdh.se/projects/wcet/benchmarks.html">Maelardalen WCET
 * Benchmarks</a>.
 */
public class DiscreteCosineTransform
{
    // Cosine Transform Coefficients
    private static final int W1 = 2841;	 // 2048*sqrt(2)*cos(1*pi/16)
    private static final int W2 = 2676;	 // 2048*sqrt(2)*cos(2*pi/16)
    private static final int W3 = 2408;	 // 2048*sqrt(2)*cos(3*pi/16)
    private static final int W5 = 1609;	 // 2048*sqrt(2)*cos(5*pi/16)
    private static final int W6 = 1108;	 // 2048*sqrt(2)*cos(6*pi/16)
    private static final int W7 = 565;	 // 2048*sqrt(2)*cos(7*pi/16)
    
    // Other FDCT Parameters
    private static final int CONST_BITS = 13;
    private static final int PASS1_BITS = 2;
    
    // Image block to be transformed
    public int[][] block = {
        {  99, 104, 109, 113, 115, 115, 55, 55 },
        { 104, 111, 113, 116, 119,  56, 56, 56 },
        { 110, 115, 120, 119, 118,  56, 56, 56 },
        { 119, 121, 122, 120, 120,  59, 59, 59 },
        { 119, 120, 121, 122, 122,  55, 55, 55 },
        { 121, 121, 121, 121,  60,  57, 57, 57 },
        { 122, 122,  61,  63,  62,  57, 57, 57 },
        {  62,  62,  61,  61,  63,  58, 58, 58 }
    };

    public void fdct(int[][] block, int lx)
    {
    	int tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7;
    	int tmp10, tmp11, tmp12, tmp13;
    	int z1, z2, z3, z4, z5;
    	int constant;

    	// Pass 1: process rows.
    	// Note that results are scaled up by sqrt(8) compared to a true DCT;
    	// furthermore, we scale the results by 2**PASS1_BITS.

        //@LoopBound(max=8)
    	for (int i = 0; i < 8; i++) // @WCA loop=8
    	{
    		tmp0 = block[i][0] + block[i][7];
    		tmp7 = block[i][0] - block[i][7];
    		tmp1 = block[i][1] + block[i][6];
    		tmp6 = block[i][1] - block[i][6];
    		tmp2 = block[i][2] + block[i][5];
    		tmp5 = block[i][2] - block[i][5];
    		tmp3 = block[i][3] + block[i][4];
    		tmp4 = block[i][3] - block[i][4];

    		// Even part per LL&M figure 1 --- note that published figure
    		// is faulty; rotator "sqrt(2)*c1" should be "sqrt(2)*c6".

    		tmp10 = tmp0 + tmp3;
    		tmp13 = tmp0 - tmp3;
    		tmp11 = tmp1 + tmp2;
    		tmp12 = tmp1 - tmp2;

    		block[i][0] = ((tmp10 + tmp11) << PASS1_BITS);
    		block[i][4] = ((tmp10 - tmp11) << PASS1_BITS);

    		constant = 4433;
    		z1 = (tmp12 + tmp13) * constant;
    		constant = 6270;
    		block[i][2] = (z1 + (tmp13 * constant)) >> (CONST_BITS - PASS1_BITS);
    		constant = -15137;
    		block[i][6] = (z1 + (tmp12 * constant)) >> (CONST_BITS - PASS1_BITS);

    		// Odd part per figure 8 --- note paper omits factor of
    		// sqrt(2). cK represents cos(K*pi/16). i0..i3 in the paper
    		// are tmp4..tmp7 here.

    		z1 = tmp4 + tmp7;
    		z2 = tmp5 + tmp6;
    		z3 = tmp4 + tmp6;
    		z4 = tmp5 + tmp7;
    		constant = 9633;
    		z5 = ((z3 + z4) * constant);  // sqrt(2) * c3

    		constant = 2446;
    		tmp4 = (tmp4 * constant);	// sqrt(2) * (-c1+c3+c5-c7)
    		constant = 16819;
    		tmp5 = (tmp5 * constant);	// sqrt(2) * ( c1+c3-c5+c7)
    		constant = 25172;
    		tmp6 = (tmp6 * constant);	// sqrt(2) * ( c1+c3+c5-c7)
    		constant = 12299;
    		tmp7 = (tmp7 * constant);	// sqrt(2) * ( c1+c3-c5-c7)
    		constant = -7373;
    		z1 = (z1 * constant);	// sqrt(2) * (c7-c3)
    		constant = -20995;
    		z2 = (z2 * constant);	// sqrt(2) * (-c1-c3)
    		constant = -16069;
    		z3 = (z3 * constant);	// sqrt(2) * (-c3-c5)
    		constant = -3196;
    		z4 = (z4 * constant);	// sqrt(2) * (c5-c3)

    		z3 += z5;
    		z4 += z5;

    		block[i][7] = (tmp4 + z1 + z3) >> (CONST_BITS - PASS1_BITS);
    		block[i][5] = (tmp5 + z2 + z4) >> (CONST_BITS - PASS1_BITS);
    		block[i][3] = (tmp6 + z2 + z3) >> (CONST_BITS - PASS1_BITS);
    		block[i][1] = (tmp7 + z1 + z4) >> (CONST_BITS - PASS1_BITS);
    	}
        
    	// Pass 2: process columns.

        //@LoopBound(max=8)
    	for (int i = 0; i < 8; i++) // @WCA loop=8
    	{
    		tmp0 = block[0][i] + block[7][i];
    		tmp7 = block[0][i] - block[7][i];
    		tmp1 = block[1][i] + block[6][i];
    		tmp6 = block[1][i] - block[6][i];
    		tmp2 = block[2][i] + block[5][i];
    		tmp5 = block[2][i] - block[5][i];
    		tmp3 = block[3][i] + block[4][i];
    		tmp4 = block[3][i] - block[4][i];

    		// Even part per LL&M figure 1 --- note that published figure
    		// is faulty; rotator "sqrt(2)*c1" should be "sqrt(2)*c6".

    		tmp10 = tmp0 + tmp3;
    		tmp13 = tmp0 - tmp3;
    		tmp11 = tmp1 + tmp2;
    		tmp12 = tmp1 - tmp2;

    		block[0][i] = (tmp10 + tmp11) >> (PASS1_BITS + 3);
    		block[4][i] = (tmp10 - tmp11) >> (PASS1_BITS + 3);

    		constant = 4433;
    		z1 = ((tmp12 + tmp13) * constant);
    		constant = 6270;
    		block[2][i] = (z1 + (tmp13 * constant)) >> (CONST_BITS + PASS1_BITS + 3);
    		constant = -15137;
    		block[6][i] = (z1 + (tmp12 * constant)) >> (CONST_BITS + PASS1_BITS + 3);

    		// Odd part per figure 8 --- note paper omits factor of
    		// sqrt(2). cK represents cos(K*pi/16). i0..i3 in the paper
    		// are tmp4..tmp7 here.

    		z1 = tmp4 + tmp7;
    		z2 = tmp5 + tmp6;
    		z3 = tmp4 + tmp6;
    		z4 = tmp5 + tmp7;
    		constant = 9633;
    		z5 = ((z3 + z4) * constant);	// sqrt(2) * c3

    		constant = 2446;
    		tmp4 = (tmp4 * constant);	// sqrt(2) * (-c1+c3+c5-c7)
    		constant = 16819;
    		tmp5 = (tmp5 * constant);	// sqrt(2) * ( c1+c3-c5+c7)
    		constant = 25172;
    		tmp6 = (tmp6 * constant);	// sqrt(2) * ( c1+c3+c5-c7)
    		constant = 12299;
    		tmp7 = (tmp7 * constant);	// sqrt(2) * ( c1+c3-c5-c7)
    		constant = -7373;
    		z1 = (z1 * constant);	// sqrt(2) * (c7-c3)
    		constant = -20995;
    		z2 = (z2 * constant);	// sqrt(2) * (-c1-c3)
    		constant = -16069;
    		z3 = (z3 * constant);	// sqrt(2) * (-c3-c5)
    		constant = -3196;
    		z4 = (z4 * constant);	// sqrt(2) * (c5-c3)

    		z3 += z5;
    		z4 += z5;

    		block[7][i] = (tmp4 + z1 + z3) >> (CONST_BITS + PASS1_BITS + 3);
    		block[5][i] = (tmp5 + z2 + z4) >> (CONST_BITS + PASS1_BITS + 3);
    		block[3][i] = (tmp6 + z2 + z3) >> (CONST_BITS + PASS1_BITS + 3);
    		block[1][i] = (tmp7 + z1 + z4) >> (CONST_BITS + PASS1_BITS + 3);
    	}
    }
    
    public static void main(String[] args)
    {
        DiscreteCosineTransform d = new DiscreteCosineTransform();

        // x8 Blocks, DC precision value = 0,
        // Quantization coefficient (mquant) = 64
        d.fdct(d.block, 8);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                System.out.println(d.block[i][j] + " ");
            }

            System.out.println();
        }
    }
}
